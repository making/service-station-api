package com.example.servicestation.ksj;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.servicestation.ServiceLocation;
import com.example.servicestation.ServiceLocationMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class KsjController {
	private final KsjLoader ksjLoader;

	private final ServiceLocationMapper serviceLocationMapper;

	public KsjController(KsjLoader ksjLoader, ServiceLocationMapper serviceLocationMapper) {
		this.ksjLoader = ksjLoader;
		this.serviceLocationMapper = serviceLocationMapper;
	}

	// https://nlftp.mlit.go.jp/ksj/jpgis/datalist/KsjTmplt-P07.html
	// のP07-**_**.xml
	// curl http://localhost:8080/ksj -F file=@P07-10_13.xml -v
	@PostMapping(path = "ksj")
	public Map<String, Object> bulkImport(@RequestParam(name = "file") MultipartFile file) throws IOException {
		final List<ServiceLocation> serviceLocations = this.ksjLoader.load(file.getInputStream()).stream()
				.map(data -> new ServiceLocation(data.getId(), data.getAddress(), data.getLatitude(), data.getLongitude()))
				.collect(Collectors.toList());
		final int updated = this.serviceLocationMapper.bulkInsert(serviceLocations);
		return Map.of("updated", updated);
	}
}
