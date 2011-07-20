package org.emonocot.checklist.model;


/**
*
* @author ben
*
*/
public enum PublicationType {

    /**
     *
     */
    BOOK(1, "Book"),

    /**
    *
    */
    JOURNAL(2, "Journal"),

    /**
    *
    */
    PERSONAL_COMMUNICATION(3, "Personal Communication"),

    /**
    *
    */
    ELECTRONIC(4, "Electronic"),

    /**
    *
    */
    SERIAL_FLORA(5, "Serial Flora"),

    /**
    *
    */
    SPECIMEN(6, "Specimen");

   /**
    *
    */
   private int id;

   /**
    *
    */
   private String name;

   /**
    *
    * @param newId Set the identifier of the enum
    * @param newName Set the name of the enum
    */
   private PublicationType(final int newId, final String newName) {
       this.id = newId;
       this.name = newName;
   }

  /**
   *
   * @param id the id of the publication type
   * @return a valid publication type
   */
  public static PublicationType fromInt(final int id) {
      for (PublicationType publicationType : PublicationType.values()) {
          if (publicationType.id == id) {
              return publicationType;
          }
      }
      throw new IllegalArgumentException(id
              + " is not a valid PublicationType id");
  }

   /**
    * @return the name of the publication type
    */
   public final String getName() {
       return name;
   }

   /**
    * @return the id of the publication type
    */
   public final int getId() {
       return id;
   }
}
