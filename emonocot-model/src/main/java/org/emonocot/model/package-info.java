/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
@org.hibernate.annotations.GenericGenerators({
	@org.hibernate.annotations.GenericGenerator(name="table-hilo", strategy="org.hibernate.id.enhanced.TableGenerator", parameters={
			@org.hibernate.annotations.Parameter(name="optimizer", value="hilo")})})

@org.hibernate.annotations.TypeDefs({
	@org.hibernate.annotations.TypeDef(name = "dateTimeUserType", typeClass = org.jadira.usertype.dateandtime.joda.PersistentDateTime.class),
	@org.hibernate.annotations.TypeDef(name = "durationUserType", typeClass = org.jadira.usertype.dateandtime.joda.PersistentDurationAsString.class),
	@org.hibernate.annotations.TypeDef(name = "termUserType", typeClass = org.emonocot.model.hibernate.TermUserType.class) })

@org.hibernate.annotations.AnyMetaDefs({
	@org.hibernate.annotations.AnyMetaDef(name ="AnnotationMetaDef", idType = "long", metaType = "string", metaValues = {
			@org.hibernate.annotations.MetaValue(targetEntity = Taxon.class, value = "Taxon"),
			@org.hibernate.annotations.MetaValue(targetEntity = Distribution.class, value = "Distribution"),
			@org.hibernate.annotations.MetaValue(targetEntity = VernacularName.class, value = "VernacularName"),
			@org.hibernate.annotations.MetaValue(targetEntity = MeasurementOrFact.class, value = "MeasurementOrFact"),
			@org.hibernate.annotations.MetaValue(targetEntity = Identifier.class, value = "Identifier"),
			@org.hibernate.annotations.MetaValue(targetEntity = JobConfiguration.class, value = "JobConfiguration"),
			@org.hibernate.annotations.MetaValue(targetEntity = TypeAndSpecimen.class, value = "TypeAndSpecimen"),
			@org.hibernate.annotations.MetaValue(targetEntity = Description.class, value = "Description"),
			@org.hibernate.annotations.MetaValue(targetEntity = Image.class, value = "Image"),
			@org.hibernate.annotations.MetaValue(targetEntity = Reference.class, value = "Reference"),
			@org.hibernate.annotations.MetaValue(targetEntity = Organisation.class, value = "Organisation"),
			@org.hibernate.annotations.MetaValue(targetEntity = Concept.class, value = "Concept")
	}),
	@org.hibernate.annotations.AnyMetaDef(name = "CommentMetaDef", idType = "long", metaType = "string", metaValues = {
			@org.hibernate.annotations.MetaValue(targetEntity = Comment.class, value = "Comment"),
			@org.hibernate.annotations.MetaValue(targetEntity = Description.class, value = "Description"),
			@org.hibernate.annotations.MetaValue(targetEntity = Distribution.class, value = "Distribution"),
			@org.hibernate.annotations.MetaValue(targetEntity = Identifier.class, value = "Identifier"),
			@org.hibernate.annotations.MetaValue(targetEntity = Image.class, value = "Image"),
			@org.hibernate.annotations.MetaValue(targetEntity = MeasurementOrFact.class, value = "MeasurementOrFact"),
			@org.hibernate.annotations.MetaValue(targetEntity = Organisation.class, value = "Organisation"),
			@org.hibernate.annotations.MetaValue(targetEntity = Resource.class, value = "Resource"),
			@org.hibernate.annotations.MetaValue(targetEntity = Reference.class, value = "Reference"),
			@org.hibernate.annotations.MetaValue(targetEntity = Taxon.class, value = "Taxon"),
			@org.hibernate.annotations.MetaValue(targetEntity = TypeAndSpecimen.class, value = "TypeAndSpecimen"),
			@org.hibernate.annotations.MetaValue(targetEntity = VernacularName.class, value = "VernacularName"),
			@org.hibernate.annotations.MetaValue(targetEntity = Concept.class, value = "Concept")
	})
})
package org.emonocot.model;

import org.emonocot.model.registry.Organisation;
import org.emonocot.model.registry.Resource;

