package org.powo.portal.view.helpers;

import org.powo.common.HtmlSanitizer;

public class TextHelper {

	public CharSequence stripHTML(String text) {
		return HtmlSanitizer.strip(text);
	}

}
