package it.univaq.disim.bpd.publicbillpostingservice.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class CityPrice {

	@NotBlank(message = "city is mandatory")
	private String city;

	@PositiveOrZero(message = "maxPrice must be zero or positive")
	private double maxPrice; // Use double for decimal numbers. BigDecimal is also an option for financial
								// precision.

	// Default constructor is essential for JSON deserialization by Jackson
	public CityPrice() {
	}

	// Optional: Constructor with all fields for convenience
	public CityPrice(String city, double maxPrice) {
		this.city = city;
		this.maxPrice = maxPrice;
	}

	// --- Getters ---
	public String getCity() {
		return city;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	// --- Setters ---
	public void setCity(String city) {
		this.city = city;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	// Optional: Override toString() for easy printing/logging
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[City and Price Info]");
		stringBuilder.append("\n");
		stringBuilder.append("city: " + city);
		stringBuilder.append("\n");
		stringBuilder.append("max price: " + maxPrice);
		return stringBuilder.toString();
	}
}