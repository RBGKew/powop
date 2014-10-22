/**
 *
 */
@org.hibernate.annotations.GenericGenerators({
  @org.hibernate.annotations.GenericGenerator(name="table-hilo", strategy="org.hibernate.id.MultipleHiLoPerTableGenerator", parameters={
		  @org.hibernate.annotations.Parameter(value="128", name="max_low")
  }),
  @org.hibernate.annotations.GenericGenerator(name = "annotation-sequence", strategy = "identity", parameters = {}) })
@org.hibernate.annotations.TypeDefs({
  @org.hibernate.annotations.TypeDef(name = "dateTimeUserType", typeClass = org.joda.time.contrib.hibernate.PersistentDateTime.class),
  @org.hibernate.annotations.TypeDef(name = "durationUserType", typeClass = org.joda.time.contrib.hibernate.PersistentDuration.class),
  @org.hibernate.annotations.TypeDef(name = "spatialType", typeClass = org.hibernatespatial.GeometryUserType.class),
  @org.hibernate.annotations.TypeDef(name = "termUserType", typeClass = org.emonocot.model.hibernate.TermUserType.class) })
@org.hibernate.annotations.AnyMetaDefs({
	@org.hibernate.annotations.AnyMetaDef(name ="AnnotationMetaDef", idType = "long", metaType = "string", metaValues = {
            @org.hibernate.annotations.MetaValue(targetEntity = Taxon.class, value = "Taxon"),
            @org.hibernate.annotations.MetaValue(targetEntity = Distribution.class, value = "Distribution"),
            @org.hibernate.annotations.MetaValue(targetEntity = VernacularName.class, value = "VernacularName"),
            @org.hibernate.annotations.MetaValue(targetEntity = MeasurementOrFact.class, value = "MeasurementOrFact"),
            @org.hibernate.annotations.MetaValue(targetEntity = Identifier.class, value = "Identifier"),
            @org.hibernate.annotations.MetaValue(targetEntity = TypeAndSpecimen.class, value = "TypeAndSpecimen"),
            @org.hibernate.annotations.MetaValue(targetEntity = Description.class, value = "Description"),
            @org.hibernate.annotations.MetaValue(targetEntity = Image.class, value = "Image"),
            @org.hibernate.annotations.MetaValue(targetEntity = Reference.class, value = "Reference"),
            @org.hibernate.annotations.MetaValue(targetEntity = Organisation.class, value = "Organisation"),
            @org.hibernate.annotations.MetaValue(targetEntity = IdentificationKey.class, value = "IdentificationKey"),
            @org.hibernate.annotations.MetaValue(targetEntity = PhylogeneticTree.class, value = "PhylogeneticTree"),
            @org.hibernate.annotations.MetaValue(targetEntity = Concept.class, value = "Concept")
    }),
    @org.hibernate.annotations.AnyMetaDef(name = "CommentMetaDef", idType = "long", metaType = "string", metaValues = {
            @org.hibernate.annotations.MetaValue(targetEntity = Comment.class, value = "Comment"),
            @org.hibernate.annotations.MetaValue(targetEntity = Description.class, value = "Description"),
            @org.hibernate.annotations.MetaValue(targetEntity = Distribution.class, value = "Distribution"),
            @org.hibernate.annotations.MetaValue(targetEntity = Identifier.class, value = "Identifier"),
            @org.hibernate.annotations.MetaValue(targetEntity = IdentificationKey.class, value = "IdentificationKey"),
            @org.hibernate.annotations.MetaValue(targetEntity = Image.class, value = "Image"),
            @org.hibernate.annotations.MetaValue(targetEntity = MeasurementOrFact.class, value = "MeasurementOrFact"),
            @org.hibernate.annotations.MetaValue(targetEntity = Organisation.class, value = "Organisation"),
            @org.hibernate.annotations.MetaValue(targetEntity = Resource.class, value = "Resource"),
            @org.hibernate.annotations.MetaValue(targetEntity = Reference.class, value = "Reference"),
            @org.hibernate.annotations.MetaValue(targetEntity = Taxon.class, value = "Taxon"),
            @org.hibernate.annotations.MetaValue(targetEntity = TypeAndSpecimen.class, value = "TypeAndSpecimen"),
            @org.hibernate.annotations.MetaValue(targetEntity = VernacularName.class, value = "VernacularName"),
            @org.hibernate.annotations.MetaValue(targetEntity = PhylogeneticTree.class, value = "PhylogeneticTree"),
            @org.hibernate.annotations.MetaValue(targetEntity = Concept.class, value = "Concept")
    })
})
package org.emonocot.model;

import org.emonocot.model.registry.Organisation;
import org.emonocot.model.registry.Resource;

