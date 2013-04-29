/**
 * 
 */
package org.emonocot.job.download;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.emonocot.model.Taxon;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

/**
 * @author jk00kg
 * Writes all taxa to a reduced list of taxa without an accepted name (Accepted, Doubtful etc.) and thier synonyms.
 * It uses a map of <Non-synonyms and Set<Synonyms>> and so should be configured with a suitable download limit
 */
public class SynonymyMappingWriter implements ItemWriter<Taxon>, StepExecutionListener {

    /**
     * 
     */
    Map<Taxon, Set<Taxon>> map;

    /* (non-Javadoc)
     * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
     */
    @Override
    public void write(List<? extends Taxon> items) throws Exception {
        for(Taxon taxon : items) {
            Taxon accepted = taxon.getAcceptedNameUsage();
            if(accepted == null) {
                map.put(taxon, new HashSet<Taxon>());
            } else if(map.containsKey(accepted)) {
                map.get(accepted).add(taxon);
            } else {
                 HashSet<Taxon> synonyms = new HashSet<Taxon>();
                synonyms.add(taxon);
                map.put(accepted, synonyms);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.core.StepExecutionListener#beforeStep(org.springframework.batch.core.StepExecution)
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        map = new HashMap<Taxon, Set<Taxon>>();
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.core.StepExecutionListener#afterStep(org.springframework.batch.core.StepExecution)
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        List<Taxon> items = new ArrayList<Taxon>(); 
        for(Taxon t : map.keySet()) {
            t.setSynonymNameUsages(map.get(t));
            items.add(t);
        }
        stepExecution.getJobExecution().getExecutionContext().put("synonymy.taxa.grouped", items);
        return ExitStatus.COMPLETED;
    }
}
