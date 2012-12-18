package org.emonocot.job.dwc.taxon;


import java.util.ArrayList;
import java.util.List;

import org.emonocot.model.Image;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.TypeAndSpecimen;
import org.springframework.batch.item.ItemWriter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TaxonDeletingWriter extends HibernateDaoSupport implements ItemWriter<Long> {

	@Override
	public void write(List<? extends Long> items) throws Exception {
		List<Taxon> taxa = new ArrayList<Taxon>();
		for(Long id : items) {
			Taxon t = getHibernateTemplate().load(Taxon.class, id);
			t.setParentNameUsage(null);
			t.setAcceptedNameUsage(null);
			t.setOriginalNameUsage(null);
			for(Reference r : t.getReferences()) {
				r.getTaxa().remove(t);
				getHibernateTemplate().update(r);
			}
			for(Image i : t.getImages()) {
				i.getTaxa().remove(t);
				if(i.getTaxon() == t) {
					i.setTaxon(null);
				}
				getHibernateTemplate().update(i);
			}
			for(TypeAndSpecimen s : t.getTypesAndSpecimens()) {
				s.getTaxa().remove(t);
				getHibernateTemplate().update(s);
			}
			taxa.add(t);
		}
		
		getHibernateTemplate().deleteAll(taxa);
		getHibernateTemplate().flush();			
	}

}
