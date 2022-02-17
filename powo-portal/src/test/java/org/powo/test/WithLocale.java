package org.powo.test;

import java.util.Locale;

public class WithLocale implements AutoCloseable {	
	private Locale original;

	public WithLocale(Locale locale) {
		this.original = Locale.getDefault();
		Locale.setDefault(locale);
	}

	@Override
	public void close() throws Exception {
		Locale.setDefault(this.original);
	}
}
