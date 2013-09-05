package org.emonocot.harvest.common;

import java.util.Map;

import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;

public class Jackson2JsonLineMapper implements LineMapper<Map<String,Object>>{
	
	private MappingJsonFactory factory = new MappingJsonFactory();

	@Override
	public Map<String, Object> mapLine(String line, int lineNumber)
			throws Exception {
		Map<String, Object> result;
		try {
			JsonParser parser = factory.createParser(line);
			@SuppressWarnings("unchecked")
			Map<String, Object> token = parser.readValueAs(Map.class);
			result = token;
		}
		catch (Exception e) {
			throw new FlatFileParseException("Cannot parse line to JSON", e, line, lineNumber);
		}
		return result;
	}

}
