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
package org.emonocot.job.key;

import java.util.List;

import org.emonocot.api.match.Match;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.harvest.common.AbstractRecordAnnotator;
import org.emonocot.model.Annotation;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.tdwg.ubif.CodedDescription;
import org.tdwg.ubif.TaxonName;

/**
 *
 * @author ben
 *
 */
public class XperTaxonNameProcessor extends AbstractRecordAnnotator implements
        ItemProcessor<CodedDescription, TaxonName> {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(XperTaxonNameProcessor.class);

    private TaxonMatcher taxonMatcher;


    /**
     * @param newTaxonMatcher
     *            the matcher to set
     */
    public final void setTaxonMatcher(final TaxonMatcher newTaxonMatcher) {
        this.taxonMatcher = newTaxonMatcher;
    }

    /**
     * @param item
     *            a taxon name to match
     * @return a lookup
     * @throws Exception
     *             if there is a problem
     */
    public final TaxonName process(final CodedDescription codedDescription) throws Exception {
    	TaxonName item = new TaxonName();
    	item.setId(codedDescription.getId());
    	item.setRepresentation(codedDescription.getRepresentation());
        Taxon object = null;
        AnnotationType annotationType = null;
        AnnotationCode code = null;
        String text = null;

        if (item.getRepresentation() == null) {
            return null;
        } else {
            String taxonName = item.getRepresentation().getLabel();            
            List<Match<Taxon>> matches = taxonMatcher.match(taxonName);
            if (matches.size() == 0) {
                annotationType = AnnotationType.Error;
                code = AnnotationCode.Absent;
                text = "No matches found for taxonomic name " + taxonName;

                item.setDebuglabel(null);
            } else if (matches.size() > 1) {
                annotationType = AnnotationType.Error;
                code = AnnotationCode.BadRecord;
                text = matches.size() + " matches found for taxonomic name "
                        + taxonName;

                item.setDebuglabel(null);
            } else {
                annotationType = AnnotationType.Info;
                code = AnnotationCode.Present;
                object = matches.get(0).getInternal();
                
                text = object.getIdentifier() + " matches taxonomic name "
                        + taxonName;

                item.setDebuglabel(object.getIdentifier());
            }

            Annotation annotation = new Annotation();
            annotation.setJobId(stepExecution.getJobExecutionId());
            annotation.setAnnotatedObj(object);
            annotation.setRecordType(RecordType.Taxon);
            annotation.setCode(code);
            annotation.setType(annotationType);
            annotation.setValue(item.getId());
            annotation.setText(text);
            super.annotate(annotation);
            return item;
        }
    }



}
