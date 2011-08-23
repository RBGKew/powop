package org.tdwg.voc;

import java.util.HashSet;
import java.util.Set;

import org.emonocot.model.taxon.Rank;
import org.tdwg.Name;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 *
 * @author ben
 *
 */
public class TaxonName extends Name {

   /**
    * Needs to go here because xstream seems to have problems
    * picking up the annotations on abstract superclasses.
    */
   @XStreamImplicit(itemFieldName = "tcomPublishedInCitation")
   private Set<PublishedInCitation> tcomPublishedInCitations = null;

    /**
     *
     */
    private String tnAuthorship;

    /**
     *
     */
    private String tnNameComplete;

    /**
     *
     */
    private String tnGenusPart;

    /**
     *
     */
    private String tnSpecificEpithet;

    /**
     *
     */
    private String tnInfraSpecificEpithet;

    /**
     *
     */
    private String tnUninomial;

    /**
    *
    */
    private String tnRankString;

    /**
     *
     */
    private TaxonRank tnRank;

    /**
     *
     */
    private String tnBasionymAuthorship;

    /**
     *
     */
    private String tnCombinationAuthorship;
    
    /**
     * Naughty, I know, but we do need to know the family of a name.
     */
    private String tnFamily;
    
    /**
     *
     * @return the authorship of this name
     */
    public final String getAuthorship() {
        return tnAuthorship;
    }

    /**
     *
     * @param authorship set the authorship of this name
     */
    public final void setAuthorship(final String authorship) {
        this.tnAuthorship = authorship;
    }

    /**
     *
     * @return the complete taxonomic name, including authorship
     */
    public final String getNameComplete() {
        return tnNameComplete;
    }

    /**
     *
     * @param nameComplete Set the complete taxonomic name
     */
    public final void setNameComplete(final String nameComplete) {
        this.tnNameComplete = nameComplete;
    }

    /**
     * @return the genus part of a bi- or trinomial name
     */
    public final String getGenusPart() {
        return tnGenusPart;
    }

    /**
     * @param genusPart the genus part of a bi- or trinomial name
     */
    public final void setGenusPart(final String genusPart) {
        this.tnGenusPart = genusPart;
    }

    /**
     * @return the specific epithet of a bi- or trinomial name
     */
    public final String getSpecificEpithet() {
        return tnSpecificEpithet;
    }

    /**
     * @param specificEpithet set the specific epithet
     */
    public final void setSpecificEpithet(final String specificEpithet) {
        this.tnSpecificEpithet = specificEpithet;
    }

    /**
     * @return the infra specific epithet of a trinomial name
     */
    public final String getInfraSpecificEpithet() {
        return tnInfraSpecificEpithet;
    }

    /**
     * @param infraSpecificEpithet
     *            set the infra specific epithet of a trinomial name
     */
    public final void setInfraSpecificEpithet(
            final String infraSpecificEpithet) {
        this.tnInfraSpecificEpithet = infraSpecificEpithet;
    }

    /**
     * @return the uninomial
     */
    public final String getUninomial() {
        return tnUninomial;
    }

    /**
     * @param uninomial set the uninomial
     */
    public final void setUninomial(final String uninomial) {
        this.tnUninomial = uninomial;
    }

    /**
     * @return the taxonomic rank of this name as a string
     */
    public final String getRankString() {
        return tnRankString;
    }

    /**
     * @param rankString the taxonomic rank of this name as a string
     */
    public final void setRankString(final String rankString) {
        this.tnRankString = rankString;
    }

    /**
     * @return the taxonomic rank of this name.
     */
    public final TaxonRankTerm getRank() {
        if (tnRank == null) {
            return null;
        } else {
            if (tnRank.getTaxonRankTerm() == null
                    && tnRank.getResource() != null) {
                tnRank.setTaxonRankTerm(
                        TaxonRankTerm.fromValue(
                                tnRank.getResource().toString()));
            }
            return tnRank.getTaxonRankTerm();
        }
    }

    /**
     * @param rank taxonomic rank of this name.
     */
    public final void setRank(final TaxonRankTerm rank) {
        if (rank != null) {
            this.tnRank = new TaxonRank(rank, true);
        } else {
            this.tnRank = null;
        }
    }

    /**
     * @return the string representing the authors of the basionym of this name
     */
    public final String getBasionymAuthorship() {
        return tnBasionymAuthorship;
    }

    /**
     * @param basionymAuthorship
     *            the string representing the authors of the basionym of this
     *            name
     */
    public final void setBasionymAuthorship(final String basionymAuthorship) {
        this.tnBasionymAuthorship = basionymAuthorship;
    }

    /**
     * @return the string representing the authors of this combination of the
     *         name
     */
    public final String getCombinationAuthorship() {
        return tnCombinationAuthorship;
    }

    /**
     * @param combinationAuthorship
     *            the string representing the authors of this combination of the
     *            name
     */
    public final void setCombinationAuthorship(
            final String combinationAuthorship) {
        this.tnCombinationAuthorship = combinationAuthorship;
    }

   /**
    *
    * @param publicationCitations Set the published in citations
    */
   public final void setPublishedInCitations(
           final Set<PublicationCitation> publicationCitations) {
       if (publicationCitations != null) {
           this.tcomPublishedInCitations = new HashSet<PublishedInCitation>();
           for (PublicationCitation publicationCitation
                   : publicationCitations) {
               tcomPublishedInCitations.add(new PublishedInCitation(
                       publicationCitation, false));
           }
       } else {
           tcomPublishedInCitations = null;
       }
   }

   /**
    *
    * @return the published in citation relation
    */
   public final Set<PublicationCitation> getPublishedInCitations() {
       if (tcomPublishedInCitations != null) {
           Set<PublicationCitation> publicationCitations
               = new HashSet<PublicationCitation>();
           for (PublishedInCitation publishedInCitation
                   : tcomPublishedInCitations) {
               publicationCitations.add(
                       publishedInCitation.getPublicationCitation());
           }
           return publicationCitations;
       } else {
           return null;
       }
   }

   /**
    * @param publicationCitation add a publication citation
    */
    public final void addPublicationCitation(
            final PublicationCitation publicationCitation) {
       if (publicationCitation != null) {
           if (this.tcomPublishedInCitations == null) {
               this.tcomPublishedInCitations
                   = new HashSet<PublishedInCitation>();
           }
           tcomPublishedInCitations.add(
                   new PublishedInCitation(publicationCitation, false));
       }
    }

    /**
     * @return the family
     */
    public final String getFamily() {
        return tnFamily;
    }

    /**
     * @param family
     *            the family to set
     */
    public final void setFamily(String family) {
        this.tnFamily = family;
    }

}
