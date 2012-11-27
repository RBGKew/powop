package org.emonocot.job.oaipmh;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.constraints.Size;

import org.apache.commons.beanutils.BeanUtils;
import org.emonocot.api.SourceService;
import org.emonocot.model.Annotation;
import org.emonocot.model.Base;
import org.emonocot.model.Reference;
import org.emonocot.model.Source;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;

/**
 * 
 * @author ben
 * 
 */
public class Validator implements ItemProcessor<Taxon, Taxon>,
		StepExecutionListener {

	/**
     *
     */
	private javax.validation.Validator validator;

	/**
    *
    */
	private Logger logger = LoggerFactory
			.getLogger(Validator.class);

	/**
     *
     */
	private String sourceName;

	/**
     *
     */
	private SourceService sourceService;

	/**
     *
     */
	private Source source;

	/**
     *
     */
	private StepExecution stepExecution;

	/**
	 * 
	 * @return the source
	 */
	private Source getSource() {
		if (source == null) {
			source = sourceService.load(sourceName);
		}
		return source;
	}

	/**
	 * @param sourceService
	 *            Set the source service;
	 */
	public final void setSourceService(SourceService sourceService) {
		this.sourceService = sourceService;
	}

	/**
	 * 
	 * @param newSourceName
	 *            Set the name of the source
	 */
	public final void setSourceName(final String newSourceName) {
		this.sourceName = newSourceName;
	}

	/**
	 * 
	 * @param validator
	 *            Set the validator
	 */
	public final void setValidator(javax.validation.Validator validator) {
		this.validator = validator;
	}

	/**
	 * @param taxon
	 *            Set the taxon
	 * @return a taxon
	 * @throws Exception
	 *             if there is a problem
	 */
	public final Taxon process(final Taxon taxon) throws Exception {
		if (taxon.isDeleted()) {
			return taxon;
		} else {
			Set<ConstraintViolation<Taxon>> violations = validator
					.validate(taxon);
			if (violations.isEmpty()) {
				logger.debug(taxon.getScientificName() + " is valid");
				if (taxon.getScientificNameAuthorship() != null) {
					logger.debug(taxon.getScientificNameAuthorship() + " ("
							+ taxon.getScientificNameAuthorship().length() + ") is valid");
				}
				return taxon;
			} else {
				logger.debug(taxon.getScientificName() + " is not valid");
				if (taxon.getScientificNameAuthorship() != null) {
					logger.debug(taxon.getScientificNameAuthorship() + " ("
							+ taxon.getScientificNameAuthorship().length() + ") is valid?");
				}
				for (ConstraintViolation<Taxon> violation : violations) {
					logger.debug(violation.getMessage());
					Object o = violation.getLeafBean();
					if (o.getClass().equals(Taxon.class)) {
						((Taxon) o).getAnnotations().add(
								addAnnotation(taxon, RecordType.Taxon,
										violation));
					} else if (o.getClass().equals(Reference.class)) {
						((Reference) o).getAnnotations().add(
								addAnnotation((Reference) o,
										RecordType.Reference, violation));
					}

				}
				return taxon;
			}
		}
	}

	/**
	 * 
	 * @param object
	 *            Set the type of object
	 * @param recordType
	 *            Set the record type
	 * @param violation
	 *            Set the constraint violation
	 * @return an annotation
	 * @throws InvocationTargetException
	 *             if there is a problem accessing a property
	 * @throws IllegalAccessException
	 *             if access is denied
	 */
	private Annotation addAnnotation(final Base object,
			final RecordType recordType, final ConstraintViolation violation)
			throws IllegalAccessException, InvocationTargetException {
		Annotation annotation = new Annotation();
		annotation.setAnnotatedObj(object);
		annotation.setJobId(getStepExecution().getJobExecutionId());
		String path = null;
		for (Path.Node node : violation.getPropertyPath()) {
			path = node.getName();
		}
		annotation.setCode(AnnotationCode.BadField);
		annotation.setRecordType(recordType);
		annotation.setValue(path);
		annotation.setType(AnnotationType.Warn);
		annotation.setAuthority(getSource());
		annotation.setText(violation.getMessage());

		if (violation.getConstraintDescriptor().getAnnotation()
				.annotationType().equals(Size.class)) {
			Integer max = (Integer) violation.getConstraintDescriptor()
					.getAttributes().get("max");
			BeanUtils.setProperty(violation.getLeafBean(), path,
					((String) violation.getInvalidValue()).substring(0, max));
		}
		return annotation;
	}

	/**
	 * 
	 * @return the step execution
	 */
	private StepExecution getStepExecution() {
		return stepExecution;
	}

	/**
	 * @param newStepExecution
	 *            Set the step execution
	 */
	public final void beforeStep(final StepExecution newStepExecution) {
		this.stepExecution = newStepExecution;
	}

	/**
	 * @param newStepExecution
	 *            set the step execution
	 * @return the exit status
	 */
	public final ExitStatus afterStep(final StepExecution newStepExecution) {
		return null;
	}

}
