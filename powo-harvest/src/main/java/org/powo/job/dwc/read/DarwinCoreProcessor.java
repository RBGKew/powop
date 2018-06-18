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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.powo.api.AnnotationService;
import org.powo.api.ResourceService;
import org.powo.api.TaxonService;
import org.powo.harvest.common.AuthorityAware;
import org.powo.job.dwc.DwCProcessingExceptionProcessListener;
import org.powo.job.dwc.exception.DarwinCoreProcessingException;
import org.powo.job.dwc.exception.InvalidValuesException;
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
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

@Scope("step")
public abstract class DarwinCoreProcessor<T extends BaseData> extends AuthorityAware implements
ItemProcessor<T, T>, ChunkListener, ItemWriteListener<T> {

	private Logger logger = LoggerFactory.getLogger(DarwinCoreProcessor.class);

	private Validator validator;

	private TaxonService taxonService;

	protected Boolean skipUnmodified = Boolean.TRUE;

	private int itemsRead;

	protected List<Annotation> chunkAnnotations = new ArrayList<>();

	@Autowired
	private AnnotationService annotationService;

	@Autowired
	private ResourceService resourceService;

	@Value("#{jobParameters['resource.identifier']}")
	private String resourceIdentifier;

	private Resource resource;

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public void setSkipUnmodified(Boolean skipUnmodified) {
		if(skipUnmodified != null) {
			this.skipUnmodified = skipUnmodified;
		}
	}

	protected void checkTaxon(RecordType recordType, Base record, Taxon taxon) throws DarwinCoreProcessingException {
		if(taxon == null) {
			throw new RequiredFieldException(record + " at line + " + getLineNumber() +  " has no Taxon set", recordType, getStepExecution().getReadCount());
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

	@Override
	public void afterChunkError(ChunkContext context) { }

	protected int getLineNumber() {
		return itemsRead;
	}

	protected Resource getResource() {
		if (resource == null) {
			resource = resourceService.find(resourceIdentifier);
			logger.debug("Found resource: {}", resource);
		}

		return resource;
	}

	protected Annotation createAnnotation(final BaseData object, RecordType recordType, AnnotationCode code, AnnotationType annotationType) {
		Annotation annotation = super.createAnnotation(object, recordType, code, annotationType);
		annotation.setResource(object.getResource());
		return annotation;
	}

	protected void replaceAnnotation(Annotated annotated, AnnotationType type, AnnotationCode code) {
		for(Annotation a : annotated.getAnnotations()) {
			if(getStepExecution().getJobExecutionId().equals(a.getJobId())) {
				a.setType(type);
				a.setCode(code);
				break;
			}
		}
	}

	@Autowired
	public void setTaxonService(TaxonService taxonService) {
		this.taxonService = taxonService;
	}

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

	public T process(T t) throws Exception {
		this.itemsRead++;
		return doProcess(t);
	}

	public abstract T doProcess(T t) throws Exception;

	@Override
	public void beforeWrite(List<? extends T> items) { }

	@Override
	public void afterWrite(List<? extends T> items) {
		chunkAnnotations.forEach(a -> annotationService.save(a));
		chunkAnnotations = new ArrayList<>();
	}

	@Override
	public void onWriteError(Exception exception, List<? extends T> items) { }

}
