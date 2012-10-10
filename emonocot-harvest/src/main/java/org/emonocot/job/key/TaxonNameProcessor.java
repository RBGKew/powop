package org.emonocot.job.key;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.emonocot.api.TaxonService;
import org.emonocot.api.match.Match;
import org.emonocot.api.match.taxon.TaxonMatcher;
import org.emonocot.harvest.common.AbstractRecordAnnotator;
import org.emonocot.model.Annotation;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.emonocot.model.util.RankBasedTaxonComparator;
import org.gbif.ecat.model.ParsedName;
import org.gbif.ecat.parser.NameParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.tdwg.ubif.TaxonName;

/**
 *
 * @author ben
 *
 */
public class TaxonNameProcessor extends AbstractRecordAnnotator implements
        ItemProcessor<TaxonName, TaxonName>, StepExecutionListener {
    /**
     *
     */
    private Logger logger = LoggerFactory.getLogger(TaxonNameProcessor.class);

    /**
    *
    */
    private TaxonMatcher taxonMatcher;

    /**
    *
    */
    private NameParser nameParser;

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    private Set<String> higherTaxa;

    /**
     *
     */
    private boolean first = true;

    /**
     * @param newTaxonMatcher
     *            the matcher to set
     */
    public final void setTaxonMatcher(final TaxonMatcher newTaxonMatcher) {
        this.taxonMatcher = newTaxonMatcher;
    }

    /**
     * @param newNameParser
     *            the nameParser to set
     */
    public final void setNameParser(final NameParser newNameParser) {
        this.nameParser = newNameParser;
    }

    /**
     * @param newTaxonService
     *            Set the taxon service
     */
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    /**
     * @param item
     *            a taxon name to match
     * @return a lookup
     * @throws Exception
     *             if there is a problem
     */
    public final TaxonName process(final TaxonName item) throws Exception {

        Taxon object = null;
        AnnotationType annotationType = null;
        AnnotationCode code = null;
        String text = null;

        if (item.getRepresentation() == null) {
            return null;
        } else {
            String taxonName = item.getRepresentation().getLabel();
            ParsedName parsedName = nameParser.parse(taxonName);
            List<Match<Taxon>> matches = taxonMatcher.match(parsedName);
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
                object = taxonService.load(object.getIdentifier(),
                        "taxon-with-ancestors");
                Set<String> ancestorIdentifiers = new HashSet<String>();
                for (Taxon ancestor : object.getHigherClassification()) {
                    ancestorIdentifiers.add(ancestor.getIdentifier());
                }
                if (first) {
                    higherTaxa.addAll(ancestorIdentifiers);
                    first = false;
                    logger.debug("Ancestors " + object.getHigherClassification());
                    logger.debug("There are " + higherTaxa.size());
                } else {
                    higherTaxa.retainAll(ancestorIdentifiers);
                    logger.debug("Ancestors " + object.getHigherClassification());
                    logger.debug("There are " + higherTaxa.size());
                }

                text = object.getIdentifier() + " matches taxonomic name "
                        + taxonName;

                item.setDebuglabel(object.getIdentifier());
            }

            Annotation annotation = new Annotation();
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

    /**
     * @param stepExecution
     *            Set the step execution
     * @return the exit status
     */
    public final ExitStatus afterStep(final StepExecution stepExecution) {
        if (!higherTaxa.isEmpty()) {
            SortedSet<Taxon> sortedHigherTaxa = new TreeSet<Taxon>(
                    new RankBasedTaxonComparator());

            for (String taxonIdentifier : higherTaxa) {
                sortedHigherTaxa.add(taxonService.load(taxonIdentifier));
            }
            Taxon t = sortedHigherTaxa.iterator().next();
            stepExecution.getJobExecution().getExecutionContext()
                    .put("root.taxon.identifier", t.getIdentifier());
            logger.debug("Root Taxon is " + t.getIdentifier());
        }
        return stepExecution.getExitStatus();
    }

    /**
     * @param stepExecution
     *            Set the step execution
     */
    public final void beforeStep(final StepExecution stepExecution) {
        first = true;
        higherTaxa = new HashSet<String>();
    }

}
