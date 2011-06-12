package org.emonocot.model.geography;

//import org.apache.lucene.spatial.base.shape.Shape;

/**
 *
 * @author ben
 *
 */
public enum Region implements GeographicalRegion {
    NORTHERN_EUROPE(10, "Northern Europe", Continent.EUROPE),
    MIDDLE_EUROPE(11, "Middle Europe", Continent.EUROPE),
    SOUTHWESTERN_EUROPE(12, "Southwestern Europe", Continent.EUROPE),
    SOUTHEASTERN_EUROPE(13, "Southeastern Europe", Continent.EUROPE),
    EASTERN_EUROPE(14, "Eastern Europe", Continent.EUROPE),
    NORTHERN_AFRICA(20, "Northern Africa", Continent.AFRICA),
    MACRONESIA(21, "Macaronesia", Continent.AFRICA),
    WEST_TROPICAL_AFRICA(22, "West Tropical Africa", Continent.AFRICA),
    WESTCENTRAL_TROPICAL_AFRICA(23,
            "West-Central Tropical Africa", Continent.AFRICA), 
    NORTHEAST_TROPICAL_AFRICA(
            24, "Northeast Tropical Africa", Continent.AFRICA),
    EAST_TROPICAL_AFRICA(25, "East Tropical Africa", Continent.AFRICA),
    SOUTH_TROPICAL_AFRICA(26, "South Tropical Africa", Continent.AFRICA),
    SOUTHERN_AFRICA(27, "Southern Africa", Continent.AFRICA),
    MIDDLE_ATLANTIC_OCEAN(28, "Middle Atlantic Ocean", Continent.AFRICA),
    WESTERN_INDIAN_OCEAN(29, "Western Indian Ocean", Continent.AFRICA),
    SIBERIA(30, "Siberia", Continent.ASIA_TEMPERATE),
    RUSSIA_FAR_EAST(31, "Russian Far East", Continent.ASIA_TEMPERATE),
    MIDDLE_ASIA(32, "Middle Asia", Continent.ASIA_TEMPERATE),
    CAUCASUS(33, "Caucasus", Continent.ASIA_TEMPERATE),
    WESTERN_ASIA(34, "Western Asia", Continent.ASIA_TEMPERATE),
    ARABIAN_PENINSULA(35, "Arabian Peninsula", Continent.ASIA_TEMPERATE),
    CHINA(36, "China", Continent.ASIA_TEMPERATE),
    MONGOLIA(37, "Mongolia", Continent.ASIA_TEMPERATE),
    EASTERN_ASIA(38, "Eastern Asia", Continent.ASIA_TEMPERATE),
    INDIAN_SUBCONTINENT(40, "Indian Subcontinent", Continent.ASIA_TROPICAL),
    INDOCHINA(41, "Indo-China", Continent.ASIA_TROPICAL),
    MALESIA(42, "Malesia", Continent.ASIA_TROPICAL),
    PAPUASIA(43, "Papuasia", Continent.ASIA_TROPICAL),
    AUSTRALASIA(50, "Australia", Continent.AUSTRALASIA),
    NEW_ZEALAND(51, "New Zealand", Continent.AUSTRALASIA),
    SOUTHWESTERN_PACIFIC(60, "Southwestern Pacific", Continent.PACIFIC),
    SOUTHCENTRAL_PACIFIC(61, "South-Central Pacific", Continent.PACIFIC),
    NORTHWESTERN_PACIFIC(62, "Northwestern Pacific", Continent.PACIFIC),
    NORTHCENTRAL_PACIFIC(63, "North-Central Pacific", Continent.PACIFIC),
    SUBARCTIC_AMERICA(70, "Subarctic America", Continent.NORTHERN_AMERICA),
    WESTERN_CANADA(71, "Western Canada", Continent.NORTHERN_AMERICA),
    EASTERN_CANADA(72, "Eastern Canada", Continent.NORTHERN_AMERICA),
    NORTHWESTERN_USA(73, "Northwestern U.S.A.", Continent.NORTHERN_AMERICA),
    NORTHCENTRAL_USA(74, "North-Central U.S.A.", Continent.NORTHERN_AMERICA),
    NORTHEASTERN_USA(75, "Northeastern U.S.A.", Continent.NORTHERN_AMERICA),
    SOUTHWESTERN_USA(76, "Southwestern U.S.A.", Continent.NORTHERN_AMERICA),
    SOUTHCENTRAL_USA(77, "South-Central U.S.A.", Continent.NORTHERN_AMERICA),
    SOUTHEASTERN_USA(78, "Southeastern U.S.A.", Continent.NORTHERN_AMERICA),
    MEXICO(79, "Mexico", Continent.NORTHERN_AMERICA),
    CENTRAL_AMERICA(80, "Central America", Continent.SOUTHERN_AMERICA),
    CARIBBEAN(81, "Caribbean", Continent.SOUTHERN_AMERICA),
    NORTHERN_SOUTH_AMERICA(82, "Northern South America", Continent.SOUTHERN_AMERICA),
    WESTERN_SOUTH_AMERICA(83, "Western South America", Continent.SOUTHERN_AMERICA),
    BRAZIL(84, "Brazil", Continent.SOUTHERN_AMERICA),
    SOUTHERN_SOUTH_AMERICA(85, "Southern South America", Continent.SOUTHERN_AMERICA),
    SUBANTARCTIC_ISLANDS(90, "Subantarctic Islands", Continent.ANTARCTICA),
    ANTARCTIC_CONTINENT(91, "Antarctic Continent", Continent.ANTARCTICA);

    /**
     *
     */
    private Integer code;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private Continent continent;

    /**
     * The geographic region as a Shape.
     */
//    private Shape shape;

    /**
     *
     * @param newCode Set the code of this region
     * @param newName Set the name of this region
     * @param newContinent Set the continent this region is in
     */
    private Region(final Integer newCode, final String newName,
            final Continent newContinent) {
        this.code = newCode;
        this.name = newName;
        this.continent = newContinent;
    }

    /**
     *
     * @param code The code of the region in question
     * @return A Region or throw an IllegalArgumentException if the code does
     *         not match to a region
     */
    public static Region fromString(final String code) {
        int c = Integer.parseInt(code);
        for (Region region : Region.values()) {
            if (c == region.code) {
                return region;
            }
        }
        throw new IllegalArgumentException(code
                + " is not a valid TDWG Level 2 Region Code");
    }

    /**
    *
    * @param code The code of the region in question
    * @return A Region or throw an IllegalArgumentException if the code does
    *         not match to a region
    */
   public static Region fromCode(final int code) {
       for (Region region : Region.values()) {
           if (code == region.code) {
               return region;
           }
       }
       throw new IllegalArgumentException(code
               + " is not a valid TDWG Level 2 Region Code");
   }

    /**
     *
     * @return The code of this region
     */
    public Integer getCode() {
        return code;
    }

    /**
     *
     * @return The name of this region
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return The continent this region is in
     */
    public Continent getContinent() {
        return continent;
    }

    @Override
    public String toString() {
        return Integer.toString(code);
    }

//    @Override
//    public Shape getShape() {
//        return shape;
//    }
//
//    @Override
//    public void setShape(final Shape newShape) {
//        this.shape = newShape;
//    }
}
