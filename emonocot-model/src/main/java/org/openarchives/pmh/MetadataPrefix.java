package org.openarchives.pmh;

/**
 *
 * @author ben
 *
 */
public enum MetadataPrefix {
    /**
     *
     */
    RDF("rdf"),
    /**
     *
     */
    OAI_DC("oai_dc");

    /**
     *
     */
    private String value;

    /**
     *
     * @param newValue The value of this metadata prefix
     */
    private MetadataPrefix(final String newValue) {
        this.value = newValue;
    }

    /**
    *
    * @param value The string value to convert
    * @return A data object type
    */
   public static MetadataPrefix fromValue(final String value) {
       for (MetadataPrefix m : MetadataPrefix.values()) {
           if (m.value.equals(value)) {
               return m;
           }
       }

       throw new IllegalArgumentException(value
               + " is not a valid string representation of MetadataPrefix");
   }

   /**
    *
    * @return The value of this metadata prefix as a string
    */
   public final String value() {
       return value;
   }

}
