package org.emonocot.model.common;

/**
 *
 * @author ben
 *
 */
public enum License {
    /**
     *
     */
    PUBLIC_DOMAIN("http://creativecommons.org/licenses/publicdomain/"),
    /**
     *
     */
    ATTRIBUTION("http://creativecommons.org/licenses/by/3.0/"),
    /**
     *
     */
    ATTRIBUTION_NONCOMMERCIAL("http://creativecommons.org/licenses/by-nc/3.0/"),
    /**
     *
     */
    ATTRIBUTION_SHAREALIKE("http://creativecommons.org/licenses/by-sa/3.0/"),
    /**
     *
     */
    ATTRIBUTION_NONCOMMERCIAL_SHAREALIKE(
            "http://creativecommons.org/licenses/by-nc-sa/3.0/");

    /**
     *
     */
    private String uri;

    /**
     *
     * @param newUri Get the uri of this license;
     */
    private License(final String newUri) {
        this.uri = newUri;
    }

    /**
     *
     * @param string The string being converted into a license
     * @return A license instance
     */
    public static License fromString(final String string) {
        for (License l : License.values()) {
            if (l.uri.equals(string)) {
                return l;
            }
        }
        throw new IllegalArgumentException(string
                + " is not an acceptable value for License");
    }
}
