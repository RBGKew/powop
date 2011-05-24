package org.tdwg.voc;

import org.tdwg.BaseThing;

/**
 *
 * @author ben
 *
 */
public class Relationship extends BaseThing {

    /**
     *
     */
    private FromTaxon tcFromTaxon;

    /**
     *
     */
    private RelationshipCategory tcRelationshipCategory;

    /**
     *
     */
    private ToTaxon tcToTaxon;

    /**
     *
     * @return the from taxon
     */
    public final TaxonConcept getFromTaxon() {
        if (tcFromTaxon != null) {
            return  tcFromTaxon.getTaxonConcept();
        } else {
            return null;
        }
    }

    /**
     *
     * @param fromTaxon Set the from taxon
     */
    public final void setFromTaxon(final TaxonConcept fromTaxon) {
        this.tcFromTaxon = new FromTaxon(fromTaxon);
    }

    /**
     *
     * @return the relationship category
     */
    public final TaxonRelationshipTerm getRelationshipCategory() {
        if (tcRelationshipCategory != null) {
            return  tcRelationshipCategory.getTaxonRelationshipTerm();
        } else {
            return null;
        }
    }

    /**
     *
     * @param relationshipCategory Set the relationship category
     */
    public final void setRelationshipCategory(
            final TaxonRelationshipTerm relationshipCategory) {
        this.tcRelationshipCategory = new RelationshipCategory(
                relationshipCategory, false);
    }

    /**
     *
     * @param relationshipCategory Set the relationship category
     */
    public final void setRelationshipCategoryRelation(
            final TaxonRelationshipTerm relationshipCategory) {
        this.tcRelationshipCategory = new RelationshipCategory(
                relationshipCategory, true);
    }

    /**
     *
     * @return the to taxon
     */
    public final TaxonConcept getToTaxon() {
        if (tcToTaxon != null) {
            return tcToTaxon.getTaxonConcept();
        } else {
            return null;
        }
    }

    /**
     *
     * @param toTaxon Set the to taxon
     */
    public final void setToTaxon(final TaxonConcept toTaxon) {
        this.tcToTaxon = new ToTaxon(toTaxon);
    }

    /**
     *
     * @return the to taxon
     */
    public final ToTaxon getToTaxonRelation() {
        return tcToTaxon;
    }

    /**
    *
    * @return the relationship category
    */
   public final RelationshipCategory getRelationshipCategoryRelation() {
       return tcRelationshipCategory;
   }
}
