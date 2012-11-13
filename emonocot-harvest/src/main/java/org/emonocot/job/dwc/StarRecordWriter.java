/**
 * 
 */
package org.emonocot.job.dwc;

import java.io.IOException;
import java.util.List;

import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.TermFactory;
import org.gbif.dwc.text.Archive;
import org.gbif.dwc.text.ArchiveField;
import org.gbif.dwc.text.ArchiveFile;
import org.gbif.dwc.text.DwcaWriter;
import org.gbif.dwc.text.StarRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

/**
 * @author jk00kg
 *
 */
public class StarRecordWriter implements ItemWriter<StarRecord>, StepExecutionListener {
    
    /**
     * 
     */
    private static final Logger logger = LoggerFactory.getLogger(TaxonToStarRecordConverter.class);

    /**
     * Used to map strings to ConceptTerm objects
     */
    protected TermFactory termFactory = new TermFactory();
    
    /**
     * 
     */
    protected DwcaWriter writer;

    /**
     * @param termFactory the termFactory to set
     */
    public final void setTermFactory(TermFactory termFactory) {
        this.termFactory = termFactory;
    }

    /**
     * @param writer the writer to set
     */
    public final void setWriter(DwcaWriter writer) {
        this.writer = writer;
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
     */
    @Override
    public void write(List<? extends StarRecord> items) throws Exception {
        for(StarRecord star : items){
            Record core = star.core();
            writer.newRecord(core.id());
            for (ConceptTerm term : core.terms()) {
                writer.addCoreColumn(term, core.value(term));
            }

            Archive arch = new Archive();
            ArchiveFile file = new ArchiveFile();
            ArchiveField field = new ArchiveField();

            // extensions
            for (String rt : star.extensions().keySet()) {
                ConceptTerm rowType = termFactory.findTerm(rt);
                ArchiveFile af = arch.getExtension(rowType);
                if(af == null) {
                    af = new ArchiveFile();
                    arch.addExtension(af);
                    af.setRowType(rt);
                }

                // iterate over records for one extension
                for (Record row : star.extension(rt)) {
                    //Make sure all fields will be added
                    for(ConceptTerm fieldTerm : row.terms()){
                        if(!af.hasTerm(fieldTerm)){
                            af.addField(new ArchiveField(null, fieldTerm, null, null));
                        }
                    }
                    writer.addExtensionRecord(rowType,DwcaWriter.recordToMap(row, af));
                }
            }
        }
        
        
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.core.StepExecutionListener#beforeStep(org.springframework.batch.core.StepExecution)
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        // Nothing to do here
        // Only interested in 'finalising' the archive at the end
        
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.core.StepExecutionListener#afterStep(org.springframework.batch.core.StepExecution)
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        try {
            writer.close();
            return stepExecution.getExitStatus();
        } catch (IOException e) {
            logger.error("Unable to close archive. Setting ExitStatus to FAILED", e);
            return ExitStatus.FAILED;
        }
    }

}
