package org.tdwg.voc;

import java.util.HashSet;
import java.util.Set;

import org.tdwg.Description;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 *
 * @author ben
 *
 */
public class SpeciesProfileModel extends Description {

    /**
     *
     */
    private AboutTaxon spmAboutTaxon;

    /**
     *
     */
    @XStreamImplicit(itemFieldName = "spmHasInformation")
    private Set<HasInformation> spmHasInformations = null;

    /**
     *
     * @return the taxon this description is about
     */
    public final TaxonConcept getAboutTaxon() {
        if (spmAboutTaxon != null) {
            return  spmAboutTaxon.getTaxonConcept();
        } else {
            return null;
        }
    }

    /**
     *
     * @param taxonConcept Set the taxon this description is about
     */
    public final void setAboutTaxon(final TaxonConcept taxonConcept) {
        this.spmAboutTaxon = new AboutTaxon(taxonConcept, true);
    }

    /**
     *
     * @return the taxon concept this description is about
     */
    public final TaxonConcept getAboutTaxonRelation() {
        if (spmAboutTaxon != null) {
            return  spmAboutTaxon.getTaxonConcept();
        } else {
            return null;
        }
    }

    /**
     *
     * @param taxonConcept the taxon concept this description is about
     */
    public final void setAboutTaxonRelation(final TaxonConcept taxonConcept) {
        this.spmAboutTaxon = new AboutTaxon(taxonConcept, true);
    }

    /**
     *
     * @return information in this description
     */
    public final Set<InfoItem> getHasInformation() {
        if (spmHasInformations != null) {
            Set<InfoItem> infoItems = new HashSet<InfoItem>();
            for (HasInformation hasInformation : spmHasInformations) {
                infoItems.add(hasInformation.getInfoItem());
            }
            return infoItems;
        } else {
            return null;
        }
    }

    /**
     *
     * @param infoItems Set information in this description
     */
    public final void setHasInformation(final Set<InfoItem> infoItems) {
        if (infoItems != null) {
            this.spmHasInformations = new HashSet<HasInformation>();
            for (InfoItem infoItem : infoItems) {
                spmHasInformations.add(new HasInformation(infoItem));
            }
        } else {
            spmHasInformations = null;
        }
    }

    /**
     *
     * @param infoItems add more information to this description
     */
    public final void addHasInformation(final Set<InfoItem> infoItems) {
        if (infoItems != null) {
            if (this.spmHasInformations == null) {
                this.spmHasInformations = new HashSet<HasInformation>();
            }
            for (InfoItem infoItem : infoItems) {
                spmHasInformations.add(new HasInformation(infoItem));
            }
        }
    }
}
