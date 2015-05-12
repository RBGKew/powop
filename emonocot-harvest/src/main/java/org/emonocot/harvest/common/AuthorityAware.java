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
package org.emonocot.harvest.common;

import org.emonocot.api.OrganisationService;
import org.emonocot.model.Annotation;
import org.emonocot.model.Base;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.emonocot.model.registry.Organisation;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class AuthorityAware implements StepExecutionListener {

    /**
     *
     */
    private StepExecution stepExecution;
    /**
        *
        */
    private OrganisationService organisationService;
    /**
       *
       */
    private Organisation source;
    /**
       *
       */
    private String sourceName;

    /**
       *
       * @param newSourceName Set the name of the Source
       */
    public final void setSourceName(final String newSourceName) {
         this.sourceName = newSourceName;
       }

    /**
        *
        * @return the Source
        */
    public final Organisation getSource() {
           if (source == null) {
               source = organisationService.load(sourceName);
           }
           return source;
       }

    /**
       *
       * @param newOrganisationService Set the source service
       */
    @Autowired
    public final void setOrganisationService(final OrganisationService newOrganisationService) {
          this.organisationService = newOrganisationService;
      }

    /**
     * @param newStepExecution Set the step exectuion
     */
    public void beforeStep(final StepExecution newStepExecution) {
        this.stepExecution = newStepExecution;
    }

    /**
     * @param newStepExecution Set the step execution
     * @return the exit status
     */
    public ExitStatus afterStep(final StepExecution newStepExecution) {
        return null;
    }

    /**
     * @return the step execution
     */
    public final StepExecution getStepExecution() {
        return stepExecution;
    }

    /**
    *
    * @param object The type of object
    * @param recordType The record type
    * @param code the code of the annotation
    * @param annotationType the type of annotation
    * @return an annotation
    */
    protected final Annotation createAnnotation(final Base object,
            final RecordType recordType, final AnnotationCode code,
            final AnnotationType annotationType) {
       Annotation annotation = new Annotation();
       annotation.setAnnotatedObj(object);
       annotation.setType(annotationType);
       annotation.setJobId(getStepExecution().getJobExecutionId());
       annotation.setCode(code);
       annotation.setRecordType(recordType);
       annotation.setAuthority(getSource());
       return annotation;
   }
}
