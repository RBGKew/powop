/**
 *
 */
@org.hibernate.annotations.TypeDefs({
        @org.hibernate.annotations.TypeDef(name = "dateTimeUserType",
            typeClass =
            org.joda.time.contrib.hibernate.PersistentDateTime.class)
})
package org.emonocot.checklist.model;

