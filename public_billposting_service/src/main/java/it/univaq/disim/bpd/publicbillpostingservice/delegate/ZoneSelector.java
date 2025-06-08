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

import it.univaq.disim.bpd.publicbillpostingservice.model.CityPrice;
import it.univaq.disim.bpd.publicbillpostingservice.model.PosterAvailabilityRequest;
import it.univaq.disim.bpd.publicbillpostingservice.model.Zone;

@Component("zoneSelectorDelegate")
public class ZoneSelector extends BaseDelegate implements JavaDelegate {

	// private static final Logger logger =
	// LoggerFactory.getLogger(ZoneSelector.class);

	@Override
	public void execute(DelegateExecution execution) {
		// logger.info("ZoneSelector Delegate Invoked");

		PosterAvailabilityRequest request = getRequiredVariable(execution, "posterAvailabilityRequest",
				PosterAvailabilityRequest.class);

		List<Zone> allZones = getRequiredVariable(execution, "zoneData", List.class);

		Map<String, List<Zone>> selectedZonesMap;
		double totalPrice;

		switch (request.getSelectionStrategy()) {
		case CHEAPEST_FIRST:
			// logger.info("Using CHEAPEST_FIRST strategy");
			selectedZonesMap = selectZonesByCheapest(allZones, request);
			break;
		case BEST_VALUE:
			// logger.info("Using BEST_VALUE strategy");
			selectedZonesMap = selectZonesByBestValue(allZones, request);
			break;
		case EQUAL_BUDGET:
			// logger.info("Using EQUAL_BUDGET strategy");
			selectedZonesMap = selectZonesWithEqualBudget(allZones, request);
			break;
		case RANDOM:
			// logger.info("Using RANDOM strategy");
			selectedZonesMap = selectZonesRandomly(allZones, request);
			break;
		case GREEDY:
		default:
			// logger.info("Using GREEDY strategy");
			selectedZonesMap = selectZonesGreedy(allZones, request);
		}

		totalPrice = selectedZonesMap.values().stream().flatMap(List::stream).mapToDouble(Zone::getPrice).sum();

		execution.setVariable("selectedZones", selectedZonesMap);
		execution.setVariable("totalPrice", totalPrice);

		// logger.info("Selected Zones by City:");
		// selectedZonesMap.forEach((city, zones) -> {
		// logger.info(" - {}", city);
		// zones.forEach(zone -> logger.info("\n{}", zone));
		// });
		// logger.info("Total price: €{}", totalPrice);
	}

	// Greedy strategy (original)
	private Map<String, List<Zone>> selectZonesGreedy(List<Zone> zones, PosterAvailabilityRequest request) {
		return selectZones(zones, request, Comparator.comparingDouble(Zone::getPrice).reversed());
	}

	// Cheapest First
	private Map<String, List<Zone>> selectZonesByCheapest(List<Zone> zones, PosterAvailabilityRequest request) {
		return selectZones(zones, request, Comparator.comparingDouble(Zone::getPrice));
	}

	// Best Value
	private Map<String, List<Zone>> selectZonesByBestValue(List<Zone> zones, PosterAvailabilityRequest request) {
		return selectZones(zones, request, Comparator.<Zone>comparingDouble(z -> z.getId() / z.getPrice()).reversed());
	}

	// Randomized selection
	private Map<String, List<Zone>> selectZonesRandomly(List<Zone> zones, PosterAvailabilityRequest request) {
		return selectZones(zones, request, (z1, z2) -> (int) (Math.random() * 3 - 1));
	}

	// Equal Budget Per City
	private Map<String, List<Zone>> selectZonesWithEqualBudget(List<Zone> zones, PosterAvailabilityRequest request) {
		Map<String, List<Zone>> zonesByCity = zones.stream().collect(Collectors.groupingBy(Zone::getCity));
		Map<String, List<Zone>> selectedZones = new HashMap<>();

		int cityCount = request.getCitiesAndMaximumPrice().size();
		double totalBudget = request.getCitiesAndMaximumPrice().stream().mapToDouble(CityPrice::getMaxPrice).sum();
		double equalBudget = totalBudget / cityCount;

		for (CityPrice cityPrice : request.getCitiesAndMaximumPrice()) {
			String city = cityPrice.getCity();
			List<Zone> cityZones = zonesByCity.getOrDefault(city, Collections.emptyList());

			cityZones.sort(Comparator.comparingDouble(Zone::getPrice).reversed());

			List<Zone> selectedInCity = new ArrayList<>();
			double citySum = 0;
			for (Zone zone : cityZones) {
				if (citySum + zone.getPrice() <= equalBudget) {
					selectedInCity.add(zone);
					citySum += zone.getPrice();
				}
			}
			if (!selectedInCity.isEmpty()) {
				selectedZones.put(city, selectedInCity);
			}
		}
		return selectedZones;
	}

	// Core selection logic by sorting strategy
	private Map<String, List<Zone>> selectZones(List<Zone> zones, PosterAvailabilityRequest request,
			Comparator<Zone> comparator) {
		Map<String, List<Zone>> zonesByCity = zones.stream().collect(Collectors.groupingBy(Zone::getCity));
		Map<String, List<Zone>> selectedZones = new HashMap<>();

		for (CityPrice cityPrice : request.getCitiesAndMaximumPrice()) {
			String city = cityPrice.getCity();
			double maxBudget = cityPrice.getMaxPrice();

			List<Zone> cityZones = zonesByCity.getOrDefault(city, Collections.emptyList());
			cityZones.sort(comparator);

			List<Zone> selectedInCity = new ArrayList<>();
			double citySum = 0;
			for (Zone zone : cityZones) {
				if (citySum + zone.getPrice() <= maxBudget) {
					selectedInCity.add(zone);
					citySum += zone.getPrice();
				}
			}

			if (!selectedInCity.isEmpty()) {
				selectedZones.put(city, selectedInCity);
			}
		}

		return selectedZones;
	}
}
