package org.emonocot.harvest.common;

import java.util.Map;

import org.springframework.batch.item.file.transform.FieldExtractor;

public class MapFieldExtractor implements FieldExtractor<Map<String,String>> {
	
	String[] names = new String[0];
	
	String quoteCharacter = "\"";

	public void setNames(String[] names) {
		this.names = names;
	}

	public void setQuoteCharacter(String quoteCharacter) {
		this.quoteCharacter = quoteCharacter;
	}

	@Override
	public Object[] extract(Map<String, String> item) {
		Object[] row = new Object[names.length];
		
		for(int i = 0; i < names.length; i++) {
			String name = names[i];
			if(item.containsKey(name)) {
			    row[i] = quoteCharacter + item.get(name) + quoteCharacter;
			}
		}
		return row;
	}

	
}
