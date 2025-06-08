package it.univaq.disim.bpd.publicbillpostingservice.model;

import java.io.Serializable;

public class Zone implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7169447481876962471L;
	private int id;
	private String name;
	private String city;
	private double price;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return String.format(
				"{\n  \"Zone Info\": {\n    \"id\": %d,\n    \"name\": \"%s\",\n    \"city\": \"%s\",\n    \"price\": %.2f\n  }\n}",
				id, name, city, price);
	}

}
