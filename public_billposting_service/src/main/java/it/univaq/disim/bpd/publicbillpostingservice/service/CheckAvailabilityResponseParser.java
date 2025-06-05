package it.univaq.disim.bpd.publicbillpostingservice.service;

import org.camunda.spin.impl.xml.dom.DomXmlElement;
import org.springframework.stereotype.Service;

import it.univaq.disim.bpd.publicbillpostingservice.model.CheckAvailabilityResponse;

@Service("checkAvailabilityResponseParserService")
public class CheckAvailabilityResponseParser {

	public CheckAvailabilityResponse unmarshallCheckAvailabilityResponse(DomXmlElement checkAvailabilityResponseXML) {
		return checkAvailabilityResponseXML.mapTo(CheckAvailabilityResponse.class);
	}

}
