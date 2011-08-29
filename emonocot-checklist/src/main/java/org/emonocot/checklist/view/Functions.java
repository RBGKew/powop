package org.emonocot.checklist.view;

/**
 *
 * @author ben
 *
 */
public final class Functions {
    /**
     *
     */
    private Functions() {
    }

    /**
     *
     * @param string Set the string to escape
     * @return an escaped string
     */
    public static String escape(final String string) {
        return string.replaceAll("&", "&amp;");
    }
}
