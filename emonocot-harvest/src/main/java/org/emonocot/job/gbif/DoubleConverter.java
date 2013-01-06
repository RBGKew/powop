package org.emonocot.job.gbif;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class DoubleConverter implements SingleValueConverter {

	@Override
	public boolean canConvert(Class type) {
		if(type.equals(Double.class)) {
			return true;
		} else {
		    return false;
		}
	}

	@Override
	public String toString(Object obj) {
		if(obj == null) {
			return null;
		} else {
			NumberFormat format = NumberFormat.getInstance(Locale.US);
			return format.format(obj);
		}
		
	}

	@Override
	public Object fromString(String str) {
		if(str == null) {
			return null;
		} else {
			NumberFormat format = NumberFormat.getInstance(Locale.US);
			try {
				return new Double(format.parse(str).doubleValue());
			} catch (ParseException pe) {
				throw new RuntimeException(pe);
			}
		}
	}

}
