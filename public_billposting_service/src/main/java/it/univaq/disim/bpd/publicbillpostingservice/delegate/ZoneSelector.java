package it.univaq.disim.bpd.publicbillpostingservice.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.univaq.disim.bpd.publicbillpostingservice.model.CityPrice;
import it.univaq.disim.bpd.publicbillpostingservice.model.PosterAvailabilityRequest;
import it.univaq.disim.bpd.publicbillpostingservice.model.Zone;

@Component("zoneSelectorDelegate")
public class ZoneSelector extends BaseDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(ZoneSelector.class);

	@Override
	public void execute(DelegateExecution execution) {
		logger.info("📍 ZoneSelector Delegate Invoked");

		PosterAvailabilityRequest request = getRequiredVariable(execution, "posterAvailabilityRequest",
				PosterAvailabilityRequest.class);
		List<Zone> allZones = getRequiredVariable(execution, "zoneData", List.class);

		// Group zones by city
		Map<String, List<Zone>> zonesByCity = allZones.stream().collect(Collectors.groupingBy(Zone::getCity));

		Map<String, List<Zone>> selectedZonesMap = new HashMap<>();
		double totalPrice = 0;

		for (CityPrice cityPrice : request.getCitiesAndMaximumPrice()) {
			String city = cityPrice.getCity();
			double maxBudget = cityPrice.getMaxPrice();

			List<Zone> zonesInCity = zonesByCity.getOrDefault(city, Collections.emptyList());
			zonesInCity.sort(Comparator.comparingDouble(Zone::getPrice).reversed());

			List<Zone> selectedInCity = new ArrayList<>();
			double sumInCity = 0;

			for (Zone zone : zonesInCity) {
				if (sumInCity + zone.getPrice() <= maxBudget) {
					selectedInCity.add(zone);
					sumInCity += zone.getPrice();
				}
			}

			if (!selectedInCity.isEmpty()) {
				selectedZonesMap.put(city, selectedInCity);
				totalPrice += sumInCity;
			}
		}

		execution.setVariable("selectedZones", selectedZonesMap);
		execution.setVariable("totalPrice", totalPrice);

		logger.info("✅ Selected Zones by City:");
		selectedZonesMap.forEach((city, zones) -> {
			logger.info("  - {}", city);
			zones.forEach(zone -> logger.info("     • {}", zone));
		});
		logger.info("💶 Total price: €{}", totalPrice);
	}

}
