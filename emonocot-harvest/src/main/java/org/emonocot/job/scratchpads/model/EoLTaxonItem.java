package org.emonocot.job.scratchpads.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 *
 * @author ben
 *
 */
@XStreamAlias("taxon")
public class EoLTaxonItem {

    /**
     * Maps to dc:identifier.
     */
    private String identifier;

    /**
     * Maps to dc:source.
     */
    private String source;

    /**
     * Maps to dwc:ScientificName.
     */
    @XStreamAlias("ScientificName")
    private String scientificName;

    /**
     * Maps to dataObject instances.
     */
    @XStreamImplicit(itemFieldName = "dataObject")
    private List<EoLDataObject> dataObjects = new ArrayList<EoLDataObject>();

    /**
     * Maps to reference instances.
     */
    @XStreamImplicit(itemFieldName = "reference")
    private List<EoLReference> references = new ArrayList<EoLReference>();

    /**
     *
     * @return The identified of this taxon.
     */
    public final String getIdentifer() {
        return identifier;
    }

    /**
     *
     * @return The data objects associated with this taxon.
     */
    public final List<EoLDataObject> getDataObjects() {
        return dataObjects;
    }

    /**
     *
     * @param url
     *            The url of the image being tested.
     * @return True if the taxon contains an image with that url or false if the
     *         taxon does not.
     */
    public final boolean containsImage(final String url) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     *
     * @return The references associated with this taxon.
     */
    public final List<EoLReference> getReferences() {
        return references;
    }

    /**
     *
     * @return The scientific name of this taxon.
     */
    public final String getScientificName() {
        return scientificName;
    }

    /**
     *
     * @return The source of this taxon.
     */
    public final String getSource() {
        return source;
    }

    /**
     *
     * @return The identifier of this taxon.
     */
    public final String getIdentifier() {
        return identifier;
    }

    /**
     *
     * @param newIdentifier Set the new identifier of this taxon.
     */
    public final void setIdentifier(final String newIdentifier) {
        this.identifier = newIdentifier;
    }

}
