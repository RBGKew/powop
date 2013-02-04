package org.emonocot.job.dwc.taxon;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.emonocot.model.Image;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.model.compare.RankBasedTaxonComparator;
import org.springframework.batch.item.ItemWriter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TaxonDeletingWriter extends HibernateDaoSupport implements ItemWriter<Long> {

	@Override
	public void write(List<? extends Long> items) throws Exception {
		List<Taxon> taxa = new ArrayList<Taxon>();
		Comparator<Taxon> comparator = new RankBasedTaxonComparator();
		for(Long id : items) {
			Taxon taxon = getHibernateTemplate().load(Taxon.class, id);
			taxon.setParentNameUsage(null);
			taxon.setAcceptedNameUsage(null);
			taxon.setOriginalNameUsage(null);
			for(Reference r : taxon.getReferences()) {
				Taxon taxonToRemove = null;
				for(Taxon t : r.getTaxa()) {
					if(t.getIdentifier().equals(taxon.getIdentifier())) {
						taxonToRemove = t;
						break;
					}
				}
				if(taxonToRemove != null) {
					r.getTaxa().remove(taxonToRemove);
				}
				
				getHibernateTemplate().update(r);
				
			}
			
			for(Image i : taxon.getImages()) {
				Taxon taxonToRemove = null;
				for(Taxon t : i.getTaxa()) {
					if(t.getIdentifier().equals(taxon.getIdentifier())) {
						taxonToRemove = t;
						break;
					}
				}
				if(taxonToRemove != null) {
					i.getTaxa().remove(taxonToRemove);
				}
				if(i.getTaxon().equals(taxon)) {
					if(i.getTaxa().isEmpty()) {
						i.setTaxon(null);
					} else if(i.getTaxa().size() == 1) {
	                    i.setTaxon(i.getTaxa().iterator().next());
	                } else {
	                    List<Taxon> sorted = new ArrayList<Taxon>(i.getTaxa());
	                    Collections.sort(sorted, comparator);
	                    i.setTaxon(sorted.get(0));
	                }
				}
				getHibernateTemplate().update(i);
			}
			
			for(TypeAndSpecimen s : taxon.getTypesAndSpecimens()) {
				Taxon taxonToRemove = null;
				for(Taxon t : s.getTaxa()) {
					if(t.getIdentifier().equals(taxon.getIdentifier())) {
						taxonToRemove = t;
						break;
					}
				}
				if(taxonToRemove != null) {
					s.getTaxa().remove(taxonToRemove);
				}
				getHibernateTemplate().update(s);
			}
			
			if (!taxon.getChildNameUsages().isEmpty()) {
                for (Taxon child : taxon.getChildNameUsages()) {
                    child.setParentNameUsage(null);
                }
                taxon.getChildNameUsages().clear();
            }
            if (!taxon.getSynonymNameUsages().isEmpty()) {
                for (Taxon synonym : taxon.getSynonymNameUsages()) {
                    synonym.setAcceptedNameUsage(null);
                }
                taxon.getSynonymNameUsages().clear();
            }
			taxa.add(taxon);
		}
		
		getHibernateTemplate().deleteAll(taxa);
		getHibernateTemplate().flush();			
	}

}
