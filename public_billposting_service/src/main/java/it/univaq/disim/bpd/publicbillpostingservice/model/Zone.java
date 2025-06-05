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
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[Zone Info]");
		stringBuilder.append("\n");
		stringBuilder.append("id: " + id);
		stringBuilder.append("\n");
		stringBuilder.append("name: " + name);
		stringBuilder.append("\n");
		stringBuilder.append("city: " + city);
		stringBuilder.append("\n");
		stringBuilder.append("price: " + price);
		return stringBuilder.toString();
	}

}
