package org.emonocot.harvest.common;

import java.util.List;

import org.emonocot.api.TaxonService;
import org.emonocot.model.Base;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateDeletingWriter<T extends Base> extends HibernateDaoSupport implements
		ItemWriter<T> {
    
    @Autowired
    TaxonService taxonService;

	public void setTaxonService(TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    @Override
	public void write(List<? extends T> items) throws Exception {
	    if (items.get(0) instanceof Reference) {//If so, they should all be
	        for (T t : items) {
                //Check all taxa?!?
	            Taxon example = new Taxon();
	            example.setNamePublishedIn((Reference) t);
                List<Taxon> linkedTaxa = taxonService.searchByExample(example, false, false).getRecords();
                for (Taxon taxon : linkedTaxa) {
                    taxon.setNamePublishedIn(null);
                }
                getHibernateTemplate().saveOrUpdateAll(linkedTaxa);
                example = new Taxon();
                example.setNameAccordingTo((Reference) t);
                linkedTaxa = taxonService.searchByExample(example, false, false).getRecords();
                for (Taxon taxon : linkedTaxa) {
                    taxon.setNameAccordingTo(null);
                }
                getHibernateTemplate().saveOrUpdateAll(linkedTaxa);
            }
	    }
		getHibernateTemplate().deleteAll(items);
	}

}
