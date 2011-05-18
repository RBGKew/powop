package org.tdwg.voc;

import org.tdwg.DefinedTerm;

/**
 *
 * @author ben
 *
 */
public class GeographicRegion extends DefinedTerm {

    /**
     *
     */
    private String grCode;

   /**
    *
    */
    private String grName;

    /**
     *
     */
    private GeographicRegion grIsPartOf;

    /**
     *
     * @return the code of this region
     */
    public final String getCode() {
        return grCode;
    }

    /**
     *
     * @param code Set the code of this region
     */
    public final void setCode(final String code) {
        this.grCode = code;
    }

    /**
     *
     * @return the name of this region
     */
    public final String getName() {
        return grName;
    }

    /**
     *
     * @param name Set the name of this region
     */
    public final void setName(final String name) {
        this.grName = name;
    }

    /**
     *
     * @return the geographic region this region is part of
     */
    public final GeographicRegion getIsPartOf() {
        return grIsPartOf;
    }

    /**
     *
     * @param isPartOf Set the region this region is part of
     */
    public final void setIsPartOf(final GeographicRegion isPartOf) {
        this.grIsPartOf = isPartOf;
    }
}
