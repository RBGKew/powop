package org.emonocot.model.geography;

import org.apache.lucene.spatial.base.shape.Shape;

import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 *
 * @author ben
 *
 */
public enum Continent implements GeographicalRegion<Continent> {
    /**
     *
     */
    EUROPE(1, "Europe","POLYGON((-2732035.7132802345 4137942.3699627547,7370519.770783563 4137942.3699627547,7370519.770783563 16048150.912687898,-2732035.7132802345 16048150.912687898,-2732035.7132802345 4137942.3699627547))"),
    /**
     *
     */
    AFRICA(2, "Africa","POLYGON((-3483186.241306008 -4139717.16544481,8070040.318979417 -4139717.16544481,8070040.318979417 4796708.160054236,-3483186.241306008 4796708.160054236,-3483186.241306008 -4139717.16544481))"),
    /**
     *
     */
    ASIA_TEMPERATE(3, "Asia-Temperate","POLYGON((-20034424.167278748 1413248.9697204947,20037497.836455688 1413248.9697204947,20037497.836455688 16850417.3918806,-20034424.167278748 16850417.3918806,-20034424.167278748 1413248.9697204947))"),
    /**
     *
     */
    ASIA_TROPICAL(4, "Asia-Tropical","POLYGON((6775595.016137074 -1368477.582735272,18118539.058314003 -1368477.582735272,18118539.058314003 4447836.759149693,6775595.016137074 4447836.759149693,6775595.016137074 -1368477.582735272))"),
    /**
     *
     */
    AUSTRALASIA(5, "Australasia","POLYGON((-19883118.71539253 -6905334.994610342,19935395.5995001 -6905334.994610342,19935395.5995001 -1124699.922810161,-19883118.71539253 -1124699.922810161,-19883118.71539253 -6905334.994610342))"),
    /**
     *
     */
    PACIFIC(6, "Pacific","POLYGON((-20037507.717173725 -3148731.0598669047,20036974.63484896 -3148731.0598669047,20036974.63484896 3276979.89126685,-20037507.717173725 3276979.89126685,-20037507.717173725 -3148731.0598669047))"),
    /**
     *
     */
    NORTHERN_AMERICA(7, "Northern America","POLYGON((-19942006.726022176 1637451.3304213618,20012729.24975419 1637451.3304213618,20012729.24975419 18418391.488670826,-19942006.726022176 18418391.488670826,-19942006.726022176 1637451.3304213618))"),
    /**
     *
     */
    SOUTHERN_AMERICA(8, "Southern America", "POLYGON((-12160206.590169305 -7542445.717661964,-3321784.111604839 -7542445.717661964,-3321784.111604839 3813586.2451123907,-12160206.590169305 3813586.2451123907,-12160206.590169305 -7542445.717661964))"),
    /**
     *
     */
    ANTARCTICA(9, "Antarctic", "POLYGON((-20037507.717173725 -107685767.74499942,20027312.103048105 -107685767.74499942,20027312.103048105 -4446426.395382819,-20037507.717173725 -4446426.395382819,-20037507.717173725 -107685767.74499942))");

    /**
     * The TDWG Code.
     */
    private Integer code;

    /**
     * The human-readable name.
     */
    private String name;

    /**
     * The geographic region as a Shape.
     */
   private Shape shape;

   /**
    *
    */
    private Polygon envelope;

    /**
     *
     * @param newCode Set the code of this continent
     * @param newName Set the name of this continent
     * @param newEnvelope set the envelope of this continent
     */
    private Continent(final int newCode, final String newName,
            final String newEnvelope) {
        this.code = newCode;
        this.name = newName;
        WKTReader wktReader = new WKTReader();
        try {
            this.envelope = (Polygon) wktReader.read(newEnvelope);           
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return The TDWG code of this continent.
     */
    public Integer getCode() {
        return code;
    }

    /**
     *
     * @return The human-readable name of this continent.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param code the code of the continent
     * @return a valid continent
     */
    public static Continent fromString(final String code) {
        int c = Integer.parseInt(code);
        for (Continent continent : Continent.values()) {
            if (continent.code == c) {
                return continent;
            }
        }
        throw new IllegalArgumentException(code
                + " is not a valid Continent code");
    }

    @Override
    public String toString() {
        return Integer.toString(code);
    }

   /**
    *
    * @param other the other region
    * @return 1 if other is after this, -1 if other is before this and 0 if
    *         other is equal to this
    */
    public int compareNames(final Continent other) {
        return this.name.compareTo(other.name);
    }

    /**
     * @return the featureId
     */
    public Integer getFeatureId() {
        return code;
    }

    /**
     * @return the envelope
     */
    public Polygon getEnvelope() {
        return envelope;
    }

    /**
     * @return the shape
     */
    public final Shape getShape() {
        return shape;
    }

    /**
     * @param newShape Set the shape
     */
    public final void setShape(final Shape newShape) {
        this.shape = newShape;
    }
}
