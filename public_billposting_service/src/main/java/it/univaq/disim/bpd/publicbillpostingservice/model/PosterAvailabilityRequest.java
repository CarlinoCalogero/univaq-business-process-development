package it.univaq.disim.bpd.publicbillpostingservice.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PosterAvailabilityRequest {

	@NotBlank(message = "username is mandatory")
	private String username;

	@NotEmpty(message = "citiesAndMaximumPrice must contain at least one city")
	@Valid
	private List<CityPrice> citiesAndMaximumPrice;

	@NotBlank(message = "posterFormat is mandatory")
	private String posterFormat;

	@NotNull(message = "selectionStrategy is mandatory")
	private ZoneSelectionStrategy selectionStrategy;

	// Default constructor is essential for JSON deserialization by Jackson
	public PosterAvailabilityRequest() {
	}

	public PosterAvailabilityRequest(String username, List<CityPrice> citiesAndMaximumPrice, String posterFormat) {
		this.username = username;
		this.citiesAndMaximumPrice = citiesAndMaximumPrice;
		this.posterFormat = posterFormat;
	}

	public String getUsername() {
		return username;
	}

	public List<CityPrice> getCitiesAndMaximumPrice() {
		return citiesAndMaximumPrice;
	}

	public String getPosterFormat() {
		return posterFormat;
	}

	public ZoneSelectionStrategy getSelectionStrategy() {
		return selectionStrategy;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setCitiesAndMaximumPrice(List<CityPrice> citiesAndMaximumPrice) {
		this.citiesAndMaximumPrice = citiesAndMaximumPrice;
	}

	public void setPosterFormat(String posterFormat) {
		this.posterFormat = posterFormat;
	}

	public void setSelectionStrategy(ZoneSelectionStrategy selectionStrategy) {
		this.selectionStrategy = selectionStrategy;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n  \"Poster Availability Request Info\": {\n");
		builder.append(String.format("    \"username\": \"%s\",\n", username));

		builder.append("    \"citiesAndMaximumPrice\": [\n");
		for (int i = 0; i < citiesAndMaximumPrice.size(); i++) {
			CityPrice cp = citiesAndMaximumPrice.get(i);
			builder.append(
					String.format("      { \"city\": \"%s\", \"maxPrice\": %.2f }", cp.getCity(), cp.getMaxPrice()));
			if (i < citiesAndMaximumPrice.size() - 1) {
				builder.append(",");
			}
			builder.append("\n");
		}
		builder.append("    ],\n");

		builder.append(String.format("    \"posterFormat\": \"%s\"\n", posterFormat));
		builder.append("  }\n}");
		return builder.toString();
	}

}