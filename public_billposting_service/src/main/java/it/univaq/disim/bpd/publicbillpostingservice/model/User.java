package it.univaq.disim.bpd.publicbillpostingservice.model;

public class User {

	private String username;
	private String name;
	private String surname;
	private String taxCode;
	private String address;
	private String city;
	private int zipCode;
	private String phone;
	private String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[User Info]");
		stringBuilder.append("\n");
		stringBuilder.append("username: " + username);
		stringBuilder.append("\n");
		stringBuilder.append("name: " + name);
		stringBuilder.append("\n");
		stringBuilder.append("surname: " + surname);
		stringBuilder.append("\n");
		stringBuilder.append("taxCode: " + taxCode);
		stringBuilder.append("\n");
		stringBuilder.append("address: " + address);
		stringBuilder.append("\n");
		stringBuilder.append("city: " + city);
		stringBuilder.append("\n");
		stringBuilder.append("zip code: " + zipCode);
		stringBuilder.append("\n");
		stringBuilder.append("phone: " + phone);
		stringBuilder.append("\n");
		stringBuilder.append("email: " + email);
		return stringBuilder.toString();
	}
}
