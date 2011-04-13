package org.emonocot.job.scratchpads.convert;

import org.emonocot.model.common.License;
import org.springframework.core.convert.converter.Converter;

public class LicenseConverter implements Converter<String,License> {

	@Override
	public License convert(String value) {
		if(value == null) {
			return null;
		}
		return License.fromString(value);
	}

}
