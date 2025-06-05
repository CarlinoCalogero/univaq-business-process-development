package it.univaq.disim.bpd.publicbillpostingservice.service;

import java.util.List;

import org.camunda.spin.json.SpinJsonNode;
import org.springframework.stereotype.Service;

import it.univaq.disim.bpd.publicbillpostingservice.model.Zone;

@Service("zoneParserService")
public class ZoneParser {

	public List<Zone> unmarshallZoneList(SpinJsonNode zoneListJSON) {
		return zoneListJSON.mapTo("java.util.ArrayList<it.univaq.disim.bpd.publicbillpostingservice.model.Zone>");
	}

}
