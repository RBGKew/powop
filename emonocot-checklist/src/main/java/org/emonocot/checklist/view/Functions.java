package org.emonocot.checklist.view;

public final class Functions {
	private Functions() {}

    public static String escape(String string) {
        return string.replaceAll("&", "&amp;");
    }
}
