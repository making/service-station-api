package com.example.servicestation.ksj;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.springframework.stereotype.Component;

/**
 * 国土数値情報ローダー
 * https://nlftp.mlit.go.jp/ksj/jpgis/datalist/KsjTmplt-P07.html
 */
@Component
public class KsjLoader {

	public Collection<Ksj> load(InputStream inputStream) {
		final XMLInputFactory factory = XMLInputFactory.newInstance();
		final Map<String, Ksj> ksjMap = new LinkedHashMap<>();
		try (final BufferedInputStream stream = new BufferedInputStream(inputStream)) {
			final XMLStreamReader reader = factory.createXMLStreamReader(stream);
			try {
				Ksj ksj = new Ksj();
				for (; reader.hasNext(); reader.next()) {
					final int eventType = reader.getEventType();
					if (eventType == XMLStreamConstants.START_ELEMENT) {
						final QName name = reader.getName();
						final String localPart = name.getLocalPart();
						if (localPart.equals("GM_Point")) {
							final String id = reader.getAttributeValue(null, "id");
							ksj.setId(id);
						}
						else if (localPart.equals("DirectPosition.coordinate")) {
							reader.next();
							if (reader.getEventType() == XMLStreamConstants.CHARACTERS) {
								final Scanner scanner = new Scanner(reader.getText());
								final double latitude = scanner.nextDouble();
								final double longitude = scanner.nextDouble();
								ksj.setLatitude(latitude);
								ksj.setLongitude(longitude);
								ksjMap.put(ksj.getId(), ksj);
								ksj = new Ksj();
							}
						}
						else if (localPart.equals("POS")) {
							final String id = reader.getAttributeValue(null, "idref");
							ksj = ksjMap.get(id);
						}
						else if (localPart.equals("ADS")) {
							reader.next();
							if (reader.getEventType() == XMLStreamConstants.CHARACTERS) {
								final String address = reader.getText();
								ksj.setAddress(address);
							}
						}
					}
				}
				return ksjMap.values();
			}
			finally {
				reader.close();
			}
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}

//	public static void main(String[] args) throws Exception {
//		final String xmlFile = "/Users/toshiaki/Downloads/spring-fest/P07-10_13/P07-10_13.xml";
//		new KsjLoader().load(new FileInputStream(xmlFile))
//				.forEach(System.out::println);
//	}
}
