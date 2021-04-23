package org.powo.portal.view.helpers;

import java.util.Arrays;

import com.github.jknack.handlebars.Options;

import org.powo.common.HtmlSanitizer;

public class TextHelper {

	public CharSequence stripHTML(String text) {
		return HtmlSanitizer.strip(text);
	}

	public String concat(String prefix, Options options) {
		String[] params = Arrays.copyOf(options.params, options.params.length, String[].class);
		return prefix + String.join("", params);
	}

}
