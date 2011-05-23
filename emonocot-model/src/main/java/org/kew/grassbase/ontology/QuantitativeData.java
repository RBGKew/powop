package org.kew.grassbase.ontology;

import org.tdwg.voc.InfoItem;

/**
 *
 * @author ben
 *
 */
public class QuantitativeData extends InfoItem {

    /**
     *
     */
    private Float grassMin;

    /**
     *
     */
    private Float grassLow;

    /**
     *
     */
    private Float grassMax;

    /**
     *
     */
    private Float grassHigh;

    /**
     *
     */
    private Float grassMean;

    /**
     *
     * @return the minimum
     */
    public final Float getMin() {
        return grassMin;
    }

    /**
     *
     * @param newMin Set the minimum
     */
    public final void setMin(final Float newMin) {
        this.grassMin = newMin;
    }

    /**
     *
     * @return the low boundary
     */
    public final Float getLow() {
        return grassLow;
    }

    /**
     *
     * @param newLow Set the low boundary
     */
    public final void setLow(final Float newLow) {
        this.grassLow = newLow;
    }

    /**
     *
     * @return the maximum
     */
    public final Float getMax() {
        return grassMax;
    }

    /**
     *
     * @param newMax Set the maximum
     */
    public final void setMax(final Float newMax) {
        this.grassMax = newMax;
    }

    /**
     *
     * @return get the high boundary
     */
    public final Float getHigh() {
        return grassHigh;
    }

    /**
     *
     * @param newHigh Set the high boundary
     */
    public final void setHigh(final Float newHigh) {
        this.grassHigh = newHigh;
    }

    /**
     *
     * @return the mean
     */
    public final Float getMean() {
        return grassMean;
    }

    /**
     *
     * @param newMean Set the mean
     */
    public final void setMean(final Float newMean) {
        this.grassMean = newMean;
    }

}
