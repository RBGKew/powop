package org.powo.portal.naturalLanguage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;

import org.powo.model.constants.DescriptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.util.csv.CSVReader;

public class CsvResourceLoader {

	Logger log = LoggerFactory.getLogger(CsvResourceLoader.class);

	private List<String[]> csvImport(String fileName) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		try (CSVReader csvReader = new CSVReader(new FileReader(file), ',', '\"', 1)) {
			return csvReader.readAll();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public EnumMap<DescriptionType, String[]> loadDescriptions(String fileName) {
		EnumMap<DescriptionType, String[]> descriptionMap = new EnumMap<>(DescriptionType.class);
		List<String[]> csv = csvImport(fileName);
		if (csv != null) {
			for (String[] strings : csv) {
				descriptionMap.put(DescriptionType.fromString(strings[0]), strings);
			}
		}
		return descriptionMap;
	}
}
