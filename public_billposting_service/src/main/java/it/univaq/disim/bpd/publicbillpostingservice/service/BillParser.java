package it.univaq.disim.bpd.publicbillpostingservice.service;

import org.camunda.spin.impl.xml.dom.DomXmlElement;
import org.springframework.stereotype.Service;

import it.univaq.disim.bpd.publicbillpostingservice.model.Bill;

@Service("billParserService")
public class BillParser {

	public Bill unmarshallBill(DomXmlElement billXML) {
		return billXML.mapTo(Bill.class);
	}

}
