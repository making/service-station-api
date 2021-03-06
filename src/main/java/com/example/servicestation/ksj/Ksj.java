package com.example.servicestation.ksj;

public class Ksj {
	private String id;

	private String address;

	private double latitude;

	private double longitude;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "Ksj{" +
				"id='" + id + '\'' +
				", address='" + address + '\'' +
				", latitude=" + latitude +
				", longitude=" + longitude +
				'}';
	}
}
