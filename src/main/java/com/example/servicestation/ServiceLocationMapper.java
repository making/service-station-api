package com.example.servicestation;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ServiceLocationMapper {
	private final NamedParameterJdbcTemplate jdbcTemplate;

	public ServiceLocationMapper(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<ServiceLocation> findAll(int limit, int offset) {
		return this.jdbcTemplate.query("SELECT id, address, location[0] AS latitude, location[1] AS longitude FROM servicelocation ORDER BY id LIMIT %d OFFSET %d".formatted(limit, offset),
				new DataClassRowMapper<>(ServiceLocation.class)
				//		(rs, rowNum) -> new ServiceLocation(rs.getString("id"), rs.getString("address"), rs.getDouble("latitude"), rs.getDouble("longitude"))
		);
	}

	public List<ServiceLocation> findNearBy(double latitude, double longitude, int limit, int offset) {
		return this.jdbcTemplate.query("SELECT id, address, location[0] AS latitude, location[1] AS longitude FROM servicelocation ORDER BY location <-> POINT(:latitude, :longitude) LIMIT %d OFFSET %d".formatted(limit, offset),
				Map.of("latitude", latitude, "longitude", longitude),
				new DataClassRowMapper<>(ServiceLocation.class)
		);
	}

	@Transactional
	public int insert(ServiceLocation serviceLocation) {
		return this.jdbcTemplate.update("INSERT INTO servicelocation(id, address, location) VALUES (:id, :address, POINT(:latitude, :longitude))",
				Map.of("id", serviceLocation.id(),
						"address", serviceLocation.address(),
						"latitude", serviceLocation.latitude(),
						"longitude", serviceLocation.longitude()));
	}

	@Transactional
	public int bulkInsert(Collection<ServiceLocation> serviceLocations) {
		final Map<String, ?>[] values = serviceLocations.stream().map(serviceLocation -> Map.of("id", serviceLocation.id(),
						"address", serviceLocation.address(),
						"latitude", serviceLocation.latitude(),
						"longitude", serviceLocation.longitude()))
				.toArray((IntFunction<Map<String, ?>[]>) Map[]::new);
		return Arrays.stream(this.jdbcTemplate.batchUpdate("INSERT INTO servicelocation(id, address, location) VALUES (:id, :address, point(:latitude, :longitude))", values))
				.sum();
	}
}
