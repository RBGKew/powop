package org.powo.portal.view.helpers;

import java.util.Arrays;

import com.github.jknack.handlebars.Options;

import org.apache.commons.text.WordUtils;
import org.powo.common.HtmlSanitizer;

public class TextHelper {

	public CharSequence stripHTML(String text) {
		return HtmlSanitizer.strip(text);
	}

	public String str(Object value) {
		return value.toString();
	}

	public String concat(String prefix, Options options) {
		String[] params = Arrays.copyOf(options.params, options.params.length, String[].class);
		return prefix + String.join("", params);
	}

	public String capitalise(Object value) {
		if (value == null) {
			return null;
		}
		return WordUtils.capitalizeFully(value.toString());
	}
}
