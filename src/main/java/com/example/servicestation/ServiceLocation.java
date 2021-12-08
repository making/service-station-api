package com.example.servicestation;

public record ServiceLocation(String id,
							  String address,
							  double latitude,
							  double longitude) {
}
