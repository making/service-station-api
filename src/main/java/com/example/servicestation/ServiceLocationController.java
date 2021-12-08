package com.example.servicestation;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceLocationController {
	private final ServiceLocationMapper serviceLocationMapper;

	public ServiceLocationController(ServiceLocationMapper serviceLocationMapper) {
		this.serviceLocationMapper = serviceLocationMapper;
	}

	@GetMapping(path = "/servicelocations")
	public List<ServiceLocation> get(@RequestParam(name = "limit", defaultValue = "10") int limit, @RequestParam(name = "offset", defaultValue = "0") int offset) {
		return this.serviceLocationMapper.findAll(limit, offset);
	}

	@GetMapping(path = "/servicelocations", params = { "latitude", "longitude" })
	public List<ServiceLocation> nearBy(
			@RequestParam(name = "latitude") double latitude, @RequestParam(name = "longitude") double longitude,
			@RequestParam(name = "limit", defaultValue = "10") int limit, @RequestParam(name = "offset", defaultValue = "0") int offset) {
		return this.serviceLocationMapper.findNearBy(latitude, longitude, limit, offset);
	}

	@PostMapping(path = "/servicelocations")
	public ServiceLocation post(@RequestBody ServiceLocation serviceLocation) {
		this.serviceLocationMapper.insert(serviceLocation);
		return serviceLocation;
	}
}
