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
package org.powo.job.dwc.read;


import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.powo.api.TaxonService;
import org.powo.harvest.common.AuthorityAware;
import org.powo.job.dwc.DwCProcessingExceptionProcessListener;
import org.powo.job.dwc.exception.DarwinCoreProcessingException;
import org.powo.job.dwc.exception.InvalidValuesException;
import org.powo.job.dwc.exception.OutOfScopeTaxonException;
import org.powo.job.dwc.exception.RequiredFieldException;
import org.powo.job.dwc.exception.WrongAuthorityException;
import org.powo.model.Annotated;
import org.powo.model.Annotation;
import org.powo.model.Base;
import org.powo.model.BaseData;
import org.powo.model.Taxon;
import org.powo.model.constants.AnnotationCode;
import org.powo.model.constants.AnnotationType;
import org.powo.model.constants.RecordType;
import org.powo.model.registry.Organisation;
import org.powo.model.registry.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author ben
 * @param <T> the type of object validated
 */
@Scope("step")
public abstract class DarwinCoreProcessor<T extends BaseData> extends AuthorityAware implements
ItemProcessor<T, T>, ChunkListener {

	private Logger logger = LoggerFactory.getLogger(DarwinCoreProcessor.class);

	private Validator validator;

	private TaxonService taxonService;

	private String family;

	private String subfamily;

	private String tribe;

	private String subtribe;

	protected Boolean skipUnmodified = Boolean.TRUE;

	private int itemsRead;

	private static final String WCS_UNPLACED_IDENTIFIER = "urn:kew.org:wcs:taxon:-9999";
	
	@Value("#{jobParameters['resource.id']}")
	private Long resourceId;
	
	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public void setSkipUnmodified(Boolean skipUnmodified) {
		if(skipUnmodified != null) {
			this.skipUnmodified = skipUnmodified;
		}
	}

	/**
	 *
	 * @param family Set the family
	 */
	public void setFamily(String family) {
		this.family = family;
	}


	/**
	 * @param subfamily the subfamily to set
	 */
	public void setSubfamily(String subfamily) {
		this.subfamily = subfamily;
	}


	/**
	 * @param tribe the tribe to set
	 */
	public void setTribe(String tribe) {
		this.tribe = tribe;
	}


	/**
	 * @param subtribe the subtribe to set
	 */
	public void setSubtribe(String subtribe) {
		this.subtribe = subtribe;
	}


	/**
	 *
	 * @param recordType Set the record type
	 * @param taxon Set the
	 * @throws DarwinCoreProcessingException
	 */
	protected void checkTaxon(RecordType recordType, Base record, Taxon taxon) throws DarwinCoreProcessingException {
		if(taxon == null) {
			throw new RequiredFieldException(record + " at line + " + getLineNumber() +  " has no Taxon set", recordType, getStepExecution().getReadCount());
		} else if(subtribe != null && subtribeOutOfScope(taxon)) {
			throw new OutOfScopeTaxonException("Expected content at line + " + getLineNumber() +  " to be related to " + subtribe + " but found content related to " + taxon + " which is in " + taxon.getSubtribe(),
					recordType, getLineNumber());
		} else if(tribe != null && tribeOutOfScope(taxon)) {
			throw new OutOfScopeTaxonException("Expected content at line + " + getLineNumber() +  " to be related to " + tribe + " but found content related to " + taxon + " which is in " + taxon.getTribe(),
					recordType, getLineNumber());
		} else if(subfamily != null && subfamilyOutOfScope(taxon)) {
			throw new OutOfScopeTaxonException("Expected content at line + " + getLineNumber() +  " to be related to " + subfamily + " but found content related to " + taxon + " which is in " + taxon.getSubfamily(),
					recordType, getLineNumber());
		} else if(family != null && familyOutOfScope(taxon)) {
			throw new OutOfScopeTaxonException("Expected content at line + " + getLineNumber() +  " to be related to " + family + " but found content related to " + taxon + " which is in " + taxon.getFamily(),
					recordType, getLineNumber());
		}
	}

	protected void  checkAuthority(RecordType recordType, Base record, Organisation authority) throws DarwinCoreProcessingException {
		if(authority.getIdentifier().equals(getSource().getIdentifier())) {
		} else {
			throw new WrongAuthorityException("Expected content at line " + getLineNumber() +  " to belong to " + getSource().getIdentifier() + " but found content belonging to " + authority.getIdentifier(), recordType, getLineNumber());
		}
	}

	@Override
	public void afterChunk(ChunkContext context) {
		logger.debug("After Chunk");
	}

	@Override
	public void beforeChunk(ChunkContext context) {
		logger.debug("Before Chunk");
		itemsRead = super.getStepExecution().getReadCount() + super.getStepExecution().getReadSkipCount();
	}

	protected int getLineNumber() {
		return itemsRead;
	}

	private boolean subtribeOutOfScope(Taxon taxon) {
		boolean outOfScope = false;
		Taxon t = taxon;
		if (t != null){
			// Traverse all the way up to the acc name
			while(t.getAcceptedNameUsage() != null){
				if (t.getAcceptedNameUsage().getIdentifier().equals(t.getIdentifier()))
					break;
				else
					t = t.getAcceptedNameUsage();
			}
			// Check for unplaced taxa, which are OK
			String identifier = t.getIdentifier();
			if (identifier != null && !t.getIdentifier().equals(WCS_UNPLACED_IDENTIFIER)){
				// If not unplaced, its acc taxon must have a null subtribe or a different one
				// to that in which we are interested to be out of scope.
				outOfScope = t.getSubtribe() == null || !t.getSubtribe().equals(subtribe);
			}
		}
		return outOfScope;
	}

	private boolean tribeOutOfScope(Taxon taxon) {
		boolean outOfScope = false;
		Taxon t = taxon;
		if (t != null){
			// Traverse all the way up to the acc name
			while(t.getAcceptedNameUsage() != null){
				if (t.getAcceptedNameUsage().getIdentifier().equals(t.getIdentifier()))
					break;
				else
					t = t.getAcceptedNameUsage();
			}
			// Check for unplaced taxa, which are OK
			String identifier = t.getIdentifier();
			if (identifier != null && !t.getIdentifier().equals(WCS_UNPLACED_IDENTIFIER)){
				// If not unplaced, its acc taxon must have a null tribe or a different one
				// to that in which we are interested to be out of scope.
				outOfScope = t.getTribe() == null || !t.getTribe().equals(tribe);
			}
		}
		return outOfScope;
	}

	private boolean subfamilyOutOfScope(Taxon taxon) {
		boolean outOfScope = false;
		Taxon t = taxon;
		if (t != null){
			// Traverse all the way up to the acc name
			while(t.getAcceptedNameUsage() != null){
				if (t.getAcceptedNameUsage().getIdentifier().equals(t.getIdentifier()))
					break;
				else
					t = t.getAcceptedNameUsage();
			}
			// Check for unplaced taxa, which are OK
			String identifier = t.getIdentifier();
			if (identifier != null && !t.getIdentifier().equals(WCS_UNPLACED_IDENTIFIER)){
				// If not unplaced, its acc taxon must have a null subfamily or a different one
				// to that in which we are interested to be out of scope.
				outOfScope = t.getSubfamily() == null || !t.getSubfamily().equals(subfamily);
			}
		}
		return outOfScope;
	}

	private boolean familyOutOfScope(Taxon taxon) {
		boolean outOfScope = false;
		Taxon t = taxon;
		if (t != null){
			// Traverse all the way up to the acc name
			while(t.getAcceptedNameUsage() != null){
				if (t.getAcceptedNameUsage().getIdentifier().equals(t.getIdentifier()))
					break;
				else
					t = t.getAcceptedNameUsage();
			}
			// Check for unplaced taxa, which are OK
			String identifier = t.getIdentifier();
			if (identifier != null && !t.getIdentifier().equals(WCS_UNPLACED_IDENTIFIER)){
				// If not unplaced, its acc taxon must have a null family or a different one
				// to that in which we are interested to be out of scope.
				outOfScope = t.getFamily() == null || !t.getFamily().equals(family);
			}
		}
		return outOfScope;
	}

	protected Annotation createAnnotation(final BaseData object, RecordType recordType, AnnotationCode code, AnnotationType annotationType) {
		Annotation annotation = super.createAnnotation(object, recordType, code, annotationType);
		annotation.setResource(object.getResource());
		return annotation;
	}

	protected void replaceAnnotation(Annotated annotated, AnnotationType type, AnnotationCode code) {
		boolean annotationPresent = false;

		for(Annotation a : annotated.getAnnotations()) {
			if(getStepExecution().getJobExecutionId().equals(a.getJobId())) {
				a.setType(type);
				a.setCode(code);
				annotationPresent = true;
				break;
			}
		}

	}

	/**
	 * @param taxonService set the taxon service
	 */
	@Autowired
	public void setTaxonService(TaxonService taxonService) {
		this.taxonService = taxonService;
	}

	/**
	 *
	 * @return the taxon service set
	 */
	public TaxonService getTaxonService() {
		return taxonService;
	}

	protected void validate(T t) {
		Set<ConstraintViolation<T>> violations = validator.validate(t);
		if(!violations.isEmpty()) {
			StepExecution stepExecution = this.getStepExecution();
			RecordType recordType = DwCProcessingExceptionProcessListener.stepNameToRecordType(stepExecution.getStepName());
			StringBuffer stringBuffer = new StringBuffer(t.getClass() + " has ");
			stringBuffer.append(violations.size()).append(" constraint violations:");
			for(ConstraintViolation<T> violation : violations) {
				stringBuffer.append(violation.getPropertyPath() +  " " + violation.getMessage());
			}
			throw new InvalidValuesException(stringBuffer.toString(), recordType,  stepExecution.getReadCount());
		}
	}

	/**
	 * @param t an object
	 * @throws Exception if something goes wrong
	 * @return an object of class T
	 */
	public T process(T t) throws Exception {
		logger.debug("resourceId is" + resourceId);
		logger.debug("object identifer is" + t.getIdentifier());
		if(resourceId != null){
			Resource resource = new Resource();
			resource.setId(resourceId);
			t.setResource(resource);
		}
		this.itemsRead++;
		return doProcess(t);
	}

	public abstract T doProcess(T t) throws Exception;
}
