package it.univaq.disim.bpd.publicbillpostingservice.model;

import spinjar.javax.xml.bind.annotation.XmlAccessType;
import spinjar.javax.xml.bind.annotation.XmlAccessorType;
import spinjar.javax.xml.bind.annotation.XmlElement;
import spinjar.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bill", namespace = "http://disim.univaq.it/services/postingservice")
@XmlAccessorType(XmlAccessType.FIELD)
public class Bill {

	@XmlElement(name = "accountHolder", namespace = "http://disim.univaq.it/services/postingservice")
	private String accountHolder;

	@XmlElement(name = "invoiceNumber", namespace = "http://disim.univaq.it/services/postingservice")
	private String invoiceNumber;

	@XmlElement(name = "amountDue", namespace = "http://disim.univaq.it/services/postingservice")
	private double amountDue;

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public double getAmountDue() {
		return amountDue;
	}

	public void setAmountDue(double amountDue) {
		this.amountDue = amountDue;
	}

	@Override
	public String toString() {
		return String.format(
				"{\n  \"Bill Info\": {\n    \"accountHolder\": \"%s\",\n    \"invoiceNumber\": \"%s\",\n    \"amountDue\": %.2f\n  }\n}",
				accountHolder, invoiceNumber, amountDue);
	}

}
