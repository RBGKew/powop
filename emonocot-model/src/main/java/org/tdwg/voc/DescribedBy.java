package org.tdwg.voc;

/**
 *
 * @author ben
 *
 */
public class DescribedBy extends LinkType {

    /**
     *
     */
    private SpeciesProfileModel spmSpeciesProfileModel;

    /**
     *
     */
    protected DescribedBy() {
    }

    /**
     *
     * @param speciesProfileModel
     *            Set the species profile model that describes this taxon
     */
    protected DescribedBy(final SpeciesProfileModel speciesProfileModel) {
        this.spmSpeciesProfileModel = speciesProfileModel;
    }

    /**
     *
     * @return the species profile model describing this taxon
     */
    protected final SpeciesProfileModel getSpeciesProfileModel() {
        return spmSpeciesProfileModel;
    }

    /**
     *
     * @param speciesProfileModel
     *            Set the species profile model describing this taxon
     */
    protected final void setSpeciesProfileModel(
            final SpeciesProfileModel speciesProfileModel) {
        this.spmSpeciesProfileModel = speciesProfileModel;
    }
}
