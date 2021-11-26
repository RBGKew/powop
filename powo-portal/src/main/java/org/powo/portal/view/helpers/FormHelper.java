package org.powo.portal.view.helpers;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Options;

public class FormHelper {

	Logger log = LoggerFactory.getLogger(FormHelper.class);

	public CharSequence radioGroup(Object obj, String field, String values, Options options) {
		StringBuilder radios = new StringBuilder();
		String labelClass = options.hash("labelClass");

		String tmpl = "<label class=\"%s\"><input type=\"radio\" name=\"%s\" value=\"%s\"%s>%s</label>";

		for(String value : values.split(",")) {
			try {
				String checked = "";
				if(BeanUtils.getSimpleProperty(obj, field).equalsIgnoreCase(value)) {
					checked = " checked";
				}
				radios.append(String.format(tmpl, labelClass, field, value, checked, WordUtils.capitalize(value)));
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				log.warn("Could not find property {} on object {}", field, obj);
			}
		}

		return new Handlebars.SafeString(radios);
	}
}
