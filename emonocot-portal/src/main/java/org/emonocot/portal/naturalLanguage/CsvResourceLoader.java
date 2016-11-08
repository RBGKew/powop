package org.emonocot.portal.naturalLanguage;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import org.emonocot.model.constants.DescriptionType;

import liquibase.util.csv.CSVReader;

public class CsvResourceLoader {

	@SuppressWarnings("unchecked")
	private List<String[]> csvImport(String fileName){
		ClassLoader classLoader = getClass().getClassLoader();
		try {
			File file = new File(classLoader.getResource(fileName).getFile());
			CSVReader csvReader = new CSVReader(new FileReader(file), ',', '\"', 1);
			return csvReader.readAll();
		} catch (Exception e) {
			// drop
		}
		return null;
	}
	
	public EnumMap<DescriptionType, String[]> loadDescriptions (String fileName){
		EnumMap<DescriptionType, String[]> descriptionMap = new EnumMap<>(DescriptionType.class);
		List<String[]> csv = csvImport(fileName);
		if(csv != null){
			for(String[] strings : csv){
				descriptionMap.put(DescriptionType.fromString(strings[0]),strings);
			}
		}
		return descriptionMap;
	}
	
}
