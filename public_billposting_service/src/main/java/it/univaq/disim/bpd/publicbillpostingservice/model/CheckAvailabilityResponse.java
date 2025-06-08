package it.univaq.disim.bpd.publicbillpostingservice.model;

import spinjar.javax.xml.bind.annotation.XmlAccessType;
import spinjar.javax.xml.bind.annotation.XmlAccessorType;
import spinjar.javax.xml.bind.annotation.XmlElement;
import spinjar.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "availabilityResponse", namespace = "http://disim.univaq.it/services/postingservice")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckAvailabilityResponse {

	@XmlElement(name = "requestId", namespace = "http://disim.univaq.it/services/postingservice")
	private String requestId;

	@XmlElement(name = "available", namespace = "http://disim.univaq.it/services/postingservice")
	private boolean available;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public String toString() {
		return String.format(
				"{\n  \"Check Availability Response Info\": {\n    \"requestId\": \"%s\",\n    \"available\": %b\n  }\n}",
				requestId, available);
	}

}
