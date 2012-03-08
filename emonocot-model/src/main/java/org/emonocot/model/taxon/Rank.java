package org.emonocot.model.taxon;

/**
 *
 * @author ben
 *
 */
public enum Rank {
    /**
     *
     */
    CULTIVAR,
    /**
     *
     */
    CULTIVAR_GROUP,
    /**
     *
     */
    GRAFT_CHIMAERA,
    /**
     *
     */
    GREX,
    /**
     *
     */
    CONVAR,
    /**
     *
     */
    DENOMINATION_CLASS,
    /**
     *
     */
    INFRASPECIFIC_TAXON,
    /**
     *
     */
    LUSUS,
    /**
     *
     */
    SPECIAL_FORM,
    /**
     *
     */
    CANDIDATE,
    /**
     *
     */
    SUB_SUB_VARIETY,
    /**
     *
     */
    SUB_VARIETY,
    /**
     *
     */
    BIO_VARIETY,
    /**
     *
     */
    PATHO_VARIETY,
    /**
     *
     */
    VARIETY("var", "Variety"),
    /**
     *
     */
    SUB_SUB_FORM,
    /**
     *
     */
    SUBFORM,
    /**
     *
     */
    FORM,
     /**
     *
     */
    INFRASPECIES("infrasp", "Infraspecies"),
   /**
    *
    */
   SUBSPECIFIC_AGGREGATE,
    /**
    *
    */
   SUBSPECIES("ssp", "Subspecies"),
   /**
    *
    */
   SPECIES_AGGREGATE,
    /**
     *
     */
    SPECIES("sp", "Species"),
    /**
     *
     */
    INFRAGENERIC_TAXON,
    /**
     *
     */
    SUBSERIES,
    /**
     *
     */
    SERIES,
    /**
     *
     */
    SUBSECTION,
    /**
     *
     */
    SECTION,
    /**
     *
     */
    INFRAGENUS,
    /**
     *
     */
    SUBGENUS,
    /**
     *
     */
    GENUS("gen", "Genus"),
    /**
     *
     */
    INFRATRIBE,
    /**
     *
     */
    SUBTRIBE,
    /**
     *
     */
    TRIBE,
    /**
     *
     */
    SUPERTRIBE,
    /**
     *
     */
    INFRAFAMILY,
    /**
     *
     */
    SUBFAMILY,
    /**
     *
     */
    FAMILY("fam", "Family"),
    /**
     *
     */
    SUPERFAMILY,
    /**
     *
     */
    INFRAORDER,
    /**
     *
     */
    SUBORDER,
    /**
     *
     */
    ORDER,
    /**
     *
     */
    SUPERORDER,
    /**
     *
     */
    INFRACLASS,
    /**
     *
     */
    SUBCLASS,
    /**
     *
     */
    CLASS,
    /**
     *
     */
    SUPERCLASS,
    /**
     *
     */
    INFRAPHYLUM,
    /**
     *
     */
    SUBPHYLUM,
    /**
     *
     */
    PHYLUM,
    /**
     *
     */
    SUPERPHYLUM,
    /**
     *
     */
    INFRADIVISION,
    /**
     *
     */
    SUBDIVISION,
    /**
     *
     */
    DIVISION,
    /**
     *
     */
    SUPERDIVISION,
    /**
     *
     */
    INFRAKINGDOM,
    /**
     *
     */
    SUBKINGDOM,
    /**
     *
     */
    KINGDOM,
    /**
     *
     */
    SUPERKINGDOM,
    /**
     *
     */
    DOMAIN,
    /**
     *
     */
    EMPIRE;

    /**
     *
     */
    private Rank() { }

    /**
    *
    */
   private String abbreviation;

   /**
    *
    */
   private String label;

   /**
    *
    * @param newAbbreviation Set the abbreviation of the rank
    * @param newLabel Set the label of the rank
    */
   private Rank(final String newAbbreviation,
           final String newLabel) {
       this.abbreviation = newAbbreviation;
       this.label = newLabel;
   }

   /**
    * @return the abbreviation
    */
   public String getAbbreviation() {
       return abbreviation;
   }

   /**
    * @return the label
    */
   public String getLabel() {
       return label;
   }
}
