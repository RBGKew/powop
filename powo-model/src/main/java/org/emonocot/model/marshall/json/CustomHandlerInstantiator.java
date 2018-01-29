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
package org.emonocot.model.marshall.json;

import org.emonocot.api.ConceptService;
import org.emonocot.api.ImageService;
import org.emonocot.api.OrganisationService;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.TaxonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

public class CustomHandlerInstantiator extends HandlerInstantiator {

	Logger logger = LoggerFactory.getLogger(CustomHandlerInstantiator.class);

	private ReferenceService referenceService;

	private TaxonService taxonService;

	private ImageService imageService;

	private OrganisationService organisationService;

	private ConceptService conceptService;

	public void setReferenceService(ReferenceService referenceService) {
		this.referenceService = referenceService;
	}

	public void setTaxonService(TaxonService taxonService) {
		this.taxonService = taxonService;
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}

	public void setConceptService(ConceptService conceptService) {
		this.conceptService = conceptService;
	}

	public CustomHandlerInstantiator() {

	}

	@Override
	public JsonDeserializer<?> deserializerInstance(
			DeserializationConfig deserializerConfig,
			Annotated annotated,
			Class<?> jsonDeserializerClass) {
		logger.debug("deserializerInstance " +  deserializerConfig + " " + jsonDeserializerClass);
		try {
			if (jsonDeserializerClass.equals(TaxonDeserializer.class)) {
				TaxonDeserializer taxonDeserializer = TaxonDeserializer.class
						.newInstance();
				taxonDeserializer.setService(taxonService);
				return taxonDeserializer;
			} else if (jsonDeserializerClass
					.equals(ReferenceDeserializer.class)) {
				ReferenceDeserializer referenceDeserializer
				= ReferenceDeserializer.class.newInstance();
				referenceDeserializer.setService(referenceService);
				return referenceDeserializer;
			} else if (jsonDeserializerClass
					.equals(ImageDeserializer.class)) {
				ImageDeserializer imageDeserializer
				= ImageDeserializer.class.newInstance();
				imageDeserializer.setService(imageService);
				return imageDeserializer;
			} else if (jsonDeserializerClass
					.equals(OrganisationDeserialiser.class)) {
				OrganisationDeserialiser sourceDeserializer
				= OrganisationDeserialiser.class.newInstance();
				sourceDeserializer.setService(organisationService);
				return sourceDeserializer;
			} else if (jsonDeserializerClass
					.equals(ConceptDeserializer.class)) {
				ConceptDeserializer conceptDeserializer
				= ConceptDeserializer.class.newInstance();
				conceptDeserializer.setService(conceptService);
				return conceptDeserializer;
			} else if (jsonDeserializerClass
					.equals(AnnotatableObjectDeserializer.class)) {
				AnnotatableObjectDeserializer annotatableObjectDeserializer
				= AnnotatableObjectDeserializer.class.newInstance();
				annotatableObjectDeserializer.addService(taxonService);
				annotatableObjectDeserializer.addService(imageService);
				annotatableObjectDeserializer.addService(referenceService);
				annotatableObjectDeserializer.addService(organisationService);
				return annotatableObjectDeserializer;
			}
		} catch (IllegalAccessException iae) {
			return null;
		} catch (InstantiationException ie) {
			return null;
		}
		return null;
	}

	@Override
	public KeyDeserializer keyDeserializerInstance(
			DeserializationConfig deserializationConfig,
			Annotated annotated,
			Class<?> keyDeserializerClass) {
		return null;
	}

	@Override
	public JsonSerializer<?> serializerInstance(
			SerializationConfig serializationConfig,
			Annotated annotated,
			Class<?> jsonSerializerClass) {
		logger.debug("serializerInstance " +  serializationConfig + " " + annotated + " " + jsonSerializerClass);
		return null;
	}

	@Override
	public TypeIdResolver typeIdResolverInstance(
			MapperConfig<?> mapperConfig, Annotated annotated,
			Class<?> typeIdResolverClass) {
		return null;
	}

	@Override
	public TypeResolverBuilder<?> typeResolverBuilderInstance(
			MapperConfig<?> mapperConfig, Annotated annotated,
			Class<?>
			typeResolverBuilderClass) {
		return null;
	}
}
