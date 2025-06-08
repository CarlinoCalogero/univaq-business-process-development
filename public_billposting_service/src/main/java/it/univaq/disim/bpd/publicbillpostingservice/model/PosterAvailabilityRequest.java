package it.univaq.disim.bpd.publicbillpostingservice.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class PosterAvailabilityRequest {

	@NotBlank(message = "username is mandatory")
	private String username;

	// This now refers to the dedicated CityPrice class
	@NotEmpty(message = "citiesAndMaximumPrice must contain at least one city")
	@Valid // validate nested CityPrice objects too
	private List<CityPrice> citiesAndMaximumPrice;

	@NotBlank(message = "posterFormat is mandatory")
	private String posterFormat;

	// Default constructor is essential for JSON deserialization by Jackson
	public PosterAvailabilityRequest() {
	}

	// Optional: Constructor with all fields for convenience
	public PosterAvailabilityRequest(String username, List<CityPrice> citiesAndMaximumPrice, String posterFormat) {
		this.username = username;
		this.citiesAndMaximumPrice = citiesAndMaximumPrice;
		this.posterFormat = posterFormat;
	}

	// --- Getters ---
	public String getUsername() {
		return username;
	}

	public List<CityPrice> getCitiesAndMaximumPrice() {
		return citiesAndMaximumPrice;
	}

	public String getPosterFormat() {
		return posterFormat;
	}

	// --- Setters ---
	public void setUsername(String username) {
		this.username = username;
	}

	public void setCitiesAndMaximumPrice(List<CityPrice> citiesAndMaximumPrice) {
		this.citiesAndMaximumPrice = citiesAndMaximumPrice;
	}

	public void setPosterFormat(String posterFormat) {
		this.posterFormat = posterFormat;
	}

	// Optional: Override toString() for easy printing/logging
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[Poster Availability Request Info]");
		stringBuilder.append("\n");
		stringBuilder.append("username: " + username);
		stringBuilder.append("\n");
		stringBuilder.append("cities and maximum prices:");
		int counter = 1;
		for (CityPrice cityAndMaxPrice : citiesAndMaximumPrice) {
			stringBuilder.append("\n");
			stringBuilder.append(
					"(" + counter + ") " + cityAndMaxPrice.getCity() + ", " + cityAndMaxPrice.getMaxPrice() + " euro");
			counter++;
		}
		stringBuilder.append("\n");
		stringBuilder.append("poster format: " + posterFormat);
		return stringBuilder.toString();
	}
}