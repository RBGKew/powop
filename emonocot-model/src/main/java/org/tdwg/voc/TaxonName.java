package org.tdwg.voc;

import org.tdwg.DefinedTerm;
import org.tdwg.Name;

/**
 *
 * @author ben
 *
 */
public class TaxonName extends Name {

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
    private DefinedTermLinkType tnRank;

    /**
     *
     */
    private String tnBasionymAuthorship;

    /**
     *
     */
    private String tnCombinationAuthorship;

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
    public final String getTnSpecificEpithet() {
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
            return (TaxonRankTerm) tnRank.getDefinedTerm();
        }
    }

    /**
     * @param rank taxonomic rank of this name.
     */
    public final void setRank(final TaxonRankTerm rank) {
        if (rank != null) {
            this.tnRank = new DefinedTermLinkType(rank, true);
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
}
