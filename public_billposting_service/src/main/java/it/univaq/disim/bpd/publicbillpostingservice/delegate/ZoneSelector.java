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
import org.springframework.stereotype.Component;

import it.univaq.disim.bpd.posteravailabilityrestservice.CityPrice;
import it.univaq.disim.bpd.posteravailabilityrestservice.PosterAvailabilityRequest;
import it.univaq.disim.bpd.publicbillpostingservice.model.Zone;

@Component("zoneSelectorDelegate")
public class ZoneSelector implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		System.out.println("Zone Selector Delegate Invoked");

		// Retrieve input variables
		PosterAvailabilityRequest request = (PosterAvailabilityRequest) execution
				.getVariable("posterAvailabilityRequest");
//		System.out.println(request);
		List<Zone> allZones = (List<Zone>) execution.getVariable("zoneData");
//		System.out.println(allZones);

		// Group all available zones by city
		Map<String, List<Zone>> zonesByCity = allZones.stream().collect(Collectors.groupingBy(Zone::getCity));

		// Prepare result map: city name → selected zones
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

		// Set results as process variables
		execution.setVariable("selectedZones", selectedZonesMap);
		execution.setVariable("totalPrice", totalPrice);

		System.out.println("✔ Selected Zones by City:");
		selectedZonesMap.forEach((city, zones) -> {
			System.out.println("- " + city + ":");
			zones.forEach(z -> System.out.println("   • " + z));
		});

		System.out.println("Total price: €" + totalPrice);
	}
}