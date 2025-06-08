package it.univaq.disim.bpd.publicbillpostingservice.service;

import org.camunda.spin.impl.xml.dom.DomXmlElement;
import org.springframework.stereotype.Service;

import it.univaq.disim.bpd.publicbillpostingservice.model.Bill;

@Service("billParserService")
public class BillParser {

	public Bill unmarshallBill(DomXmlElement billXML) {
		if (billXML == null) {
            throw new IllegalStateException("Missing required DomXmlElement for Bill");
        }
		return billXML.mapTo(Bill.class);
	}

}
