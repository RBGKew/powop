package org.emonocot.model.description;

/**
 *
 * @author ben
 *
 */
public enum Feature {
    /**
     *
     */
    GENERAL_DESCRIPTION("http://rs.tdwg.org/ontology/voc/"
            + "SPMInfoItems#GeneralDescription");

    /**
     *
     */
    private String uri;

    /**
     *
     * @param newUri The uri of this feature
     */
    private Feature(final String newUri) {
        this.uri = newUri;
    }

    /**
     *
     * @param uri The uri being converted into a Feature
     * @return the matching feature or throw an IllegalArgumentException if no
     *         feature matches
     */
    public static Feature fromString(final String uri) {
        for (Feature f : Feature.values()) {
            if (f.uri.equals(uri)) {
                return f;
            }
        }
        throw new IllegalArgumentException(uri
                + " is not an acceptable value for Feature");
    }

}
