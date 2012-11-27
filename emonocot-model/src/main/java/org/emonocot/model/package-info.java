/**
 *
 */
@org.hibernate.annotations.GenericGenerators({
  @org.hibernate.annotations.GenericGenerator(name = "system-increment", strategy = "increment"),
  @org.hibernate.annotations.GenericGenerator(name = "annotation-sequence", strategy = "identity", parameters = {}) })
@org.hibernate.annotations.TypeDefs({
  @org.hibernate.annotations.TypeDef(name = "dateTimeUserType", typeClass = org.joda.time.contrib.hibernate.PersistentDateTime.class),
  @org.hibernate.annotations.TypeDef(name = "durationUserType", typeClass = org.joda.time.contrib.hibernate.PersistentDuration.class),
  @org.hibernate.annotations.TypeDef(name = "olapDateTime", typeClass = org.emonocot.model.hibernate.OlapDateTimeUserType.class),
  @org.hibernate.annotations.TypeDef(name = "tdwgRegionUserType", typeClass = org.emonocot.model.hibernate.GeographicRegionUserType.class),
  @org.hibernate.annotations.TypeDef(name = "spatialType", typeClass = org.hibernatespatial.GeometryUserType.class) })
package org.emonocot.model;

