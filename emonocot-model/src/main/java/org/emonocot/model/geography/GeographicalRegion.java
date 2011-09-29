package org.emonocot.model.geography;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public interface GeographicalRegion<T extends GeographicalRegion> {
    /**
     *
     * @return the geographical region code
     */
    Object getCode();

    /**
     *
     * @param other the other region
     * @return 1 if other is after this, -1 if other is before this and 0 if
     *         other is equal to this
     */
    int compareNames(T other);

}
