package it.univaq.disim.bpd.publicbillpostingservice.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class CityPrice {

	@NotBlank(message = "city is mandatory")
	private String city;

	@PositiveOrZero(message = "maxPrice must be zero or positive")
	private double maxPrice;

	// Default constructor is essential for JSON deserialization by Jackson
	public CityPrice() {
	}

	public CityPrice(String city, double maxPrice) {
		this.city = city;
		this.maxPrice = maxPrice;
	}

	public String getCity() {
		return city;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	@Override
	public String toString() {
		return String.format("{\n  \"City and Price Info\": {\n    \"city\": \"%s\",\n    \"maxPrice\": %.2f\n  }\n}",
				city, maxPrice);
	}

}