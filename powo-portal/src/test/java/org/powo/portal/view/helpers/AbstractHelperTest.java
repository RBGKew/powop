package org.powo.portal.view.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.StringTemplateSource;
import com.github.jknack.handlebars.io.TemplateSource;
import com.github.jknack.handlebars.io.URLTemplateLoader;

/*
 * Copied mostly verbatim from 
 *  - https://github.com/jknack/handlebars.java/blob/master/handlebars/src/test/java/com/github/jknack/handlebars/AbstractTest.java
 *  - https://github.com/jknack/handlebars.java/blob/master/handlebars/src/test/java/com/github/jknack/handlebars/MapTemplateLoader.java
 */
public class AbstractHelperTest {
	class MapTemplateLoader extends URLTemplateLoader {

		private Map<String, String> map;

		public MapTemplateLoader(final Map<String, String> map) {
			this.map = map;
		}

		public MapTemplateLoader() {
			this(new HashMap<String, String>());
		}

		public MapTemplateLoader define(final String name, final String content) {
			map.put(getPrefix() + name + getSuffix(), content);
			return this;
		}

		@Override
		public TemplateSource sourceAt(final String uri) throws FileNotFoundException {
			String location = resolve(normalize(uri));
			String text = map.get(location);
			if (text == null) {
				throw new FileNotFoundException(location);
			}
			return new StringTemplateSource(location, text);
		}

		@Override
		protected URL getResource(final String location) {
			throw new UnsupportedOperationException();
		}

	}

	@SuppressWarnings("serial")
	public static class Hash extends LinkedHashMap<String, Object> {

		public Hash $(final String name, final Object value) {
			put(name, value);
			return this;
		}
	}

	public void shouldCompileTo(final String template, final String data,
			final String expected) throws IOException {
		shouldCompileTo(template, data, expected, "");
	}

	public void shouldCompileTo(final String template, final Object data,
			final String expected) throws IOException {
		shouldCompileTo(template, data, expected, "");
	}

	public void shouldCompileTo(final String template, final String context,
			final String expected, final String message) throws IOException {
		Object deserializedContext = context;
		if (deserializedContext != null) {
			deserializedContext = new Yaml().load(context);
		}
		shouldCompileTo(template, deserializedContext, expected, message);
	}

	public void shouldCompileTo(final String template, final Object context,
			final String expected, final String message) throws IOException {
		shouldCompileTo(template, context, new Hash(), expected, message);
	}

	public void shouldCompileTo(final String template, final Object context,
			final Hash helpers, final String expected) throws IOException {
		shouldCompileTo(template, context, helpers, expected, "");
	}

	public void shouldCompileTo(final String template, final String context,
			final Hash helpers, final String expected) throws IOException {
		shouldCompileTo(template, new Yaml().load(context), helpers, expected, "");
	}

	public void shouldCompileTo(final String template, final String context,
			final Hash helpers, final String expected, final String message) throws IOException {
		shouldCompileTo(template, new Yaml().load(context), helpers, expected, message);
	}

	public void shouldCompileTo(final String template, final Object context,
			final Hash helpers, final String expected, final String message) throws IOException {
		shouldCompileTo(template, context, helpers, new Hash(), expected, message);
	}

	public void shouldCompileToWithPartials(final String template, final Object context,
			final Hash partials, final String expected) throws IOException {
		shouldCompileTo(template, context, new Hash(), partials, expected, "");
	}

	public void shouldCompileToWithPartials(final String template, final Object context,
			final Hash partials, final String expected, final String message) throws IOException {
		shouldCompileTo(template, context, new Hash(), partials, expected, message);
	}

	public void shouldCompileTo(final String template, final Object context,
			final Hash helpers, final Hash partials, final String expected, final String message)
					throws IOException {
		Template t = compile(template, helpers, partials);
		String result = t.apply(configureContext(context));
		org.junit.Assert.assertEquals("'" + expected + "' should === '" + result + "': " + message, expected, result);
	}


	/**
	 * Helper to execute the given template with the provided data, returning the rendered content
	 * @param template
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public String renderTemplate(final String template, final Object data) throws IOException {
		Template t = compile(template, new Hash(), new Hash());
		return t.apply(data);
	}

	protected Object configureContext(final Object context) {
		return context;
	}

	public Template compile(final String template) throws IOException {
		return compile(template, new Hash());
	}

	public Template compile(final String template, final Hash helpers)
			throws IOException {
		return compile(template, helpers, new Hash(), false);
	}

	public Template compile(final String template, final Hash helpers, final boolean stringParams)
			throws IOException {
		return compile(template, helpers, new Hash(), stringParams);
	}

	public Template compile(final String template, final Hash helpers, final Hash partials)
			throws IOException {
		return compile(template, helpers, partials, false);
	}

	public Template compile(final String template, final Hash helpers, final Hash partials,
			final boolean stringParams)
					throws IOException {
		MapTemplateLoader loader = new MapTemplateLoader();
		for (Entry<String, Object> entry : partials.entrySet()) {
			loader.define(entry.getKey(), (String) entry.getValue());
		}
		Handlebars handlebars = newHandlebars().with(loader);
		configure(handlebars);
		handlebars.setStringParams(stringParams);

		for (Entry<String, Object> entry : helpers.entrySet()) {
			final Object value = entry.getValue();
			final Helper<?> helper;
			if (!(value instanceof Helper)) {
				helper = new Helper<Object>() {
					@Override
					public CharSequence apply(final Object context, final Options options) throws IOException {
						return value.toString();
					}
				};
			} else {
				helper = (Helper<?>) value;
			}
			handlebars.registerHelper(entry.getKey(), helper);
		}
		Template t = handlebars.compileInline(template);
		return t;
	}

	protected void configure(final Handlebars handlebars) {
	}

	protected Handlebars newHandlebars() {
		return new Handlebars();
	}

	public static final Object $ = new Object();

	public static Hash $(final Object... attributes) {
		Hash model = new Hash();
		for (int i = 0; i < attributes.length; i += 2) {
			model.$((String) attributes[i], attributes[i + 1]);
		}
		return model;
	}
}