package org.emonocot.job.dwc.phylo;

import org.emonocot.api.PhylogeneticTreeService;
import org.emonocot.job.dwc.read.NonOwnedProcessor;
import org.emonocot.model.PhylogeneticTree;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is slightly different from the description validator because we believe
 * (or at least, I believe) that descriptive content is somehow "owned" or
 * "contained in" the taxon i.e. that if the taxon does not exist, the
 * descriptive content doesn't either. There is a one-to-many relationship
 * between taxa and descriptive content because there can be many facts about
 * each taxon, but each fact is only about one taxon.
 *
 * Images, on the other hand, have an inherently many-to-many relationship with
 * taxa as one image can appear on several different taxon pages (especially the
 * family page, type genus page, type species page etc). Equally a taxon page
 * can have many images on it. So Images don't belong to any one taxon page
 * especially. If we delete the taxon, the image can hang around - it might have
 * value on its own.
 *
 * @author ben
 *
 */
public class Processor extends NonOwnedProcessor<PhylogeneticTree, PhylogeneticTreeService> {

    private Logger logger = LoggerFactory.getLogger(Processor.class);

    @Autowired
    public final void setPhylogeneticTreeService(PhylogeneticTreeService PhylogeneticTreeService) {
        super.service = PhylogeneticTreeService;
    } 

	@Override
	protected void doUpdate(PhylogeneticTree persisted, PhylogeneticTree t) {
		persisted.setCreator(t.getCreator());
		persisted.setDescription(t.getDescription());
		persisted.setTitle(t.getTitle());
	}

	@Override
	protected void doPersist(PhylogeneticTree t) {
		
	}

	@Override
	protected RecordType getRecordType() {
		return RecordType.PhylogeneticTree;
	}

	@Override
	protected void bind(PhylogeneticTree t) {
		boundObjects.put(t.getIdentifier(), t);
	}

	@Override
	protected PhylogeneticTree retrieveBound(PhylogeneticTree t) {
		return service.find(t.getIdentifier());
	}

	@Override
	protected PhylogeneticTree lookupBound(PhylogeneticTree t) {
		return boundObjects.get(t.getIdentifier());
	}

	@Override
	protected void doValidate(PhylogeneticTree t) throws Exception {
		
	}

	@Override
	protected boolean doFilter(PhylogeneticTree t) {
		return false;
	}

}
