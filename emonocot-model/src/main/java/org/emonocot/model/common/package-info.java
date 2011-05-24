/**
 *
 */
@org.hibernate.annotations.GenericGenerator(
                name = "system-increment",
                strategy = "increment"
)
@org.hibernate.annotations.TypeDefs({
        @org.hibernate.annotations.TypeDef(name = "dateTimeUserType",
            typeClass =
            org.joda.time.contrib.hibernate.PersistentDateTime.class),
        @org.hibernate.annotations.TypeDef(name = "tdwgRegionUserType",
                    typeClass =
                    org.emonocot.model.hibernate.GeographicRegionUserType.class)
})
package org.emonocot.model.common;
