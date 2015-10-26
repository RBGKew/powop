package org.emonocot.job.dwc.taxon;

import java.util.List;

import org.emonocot.model.Taxon;
import org.springframework.batch.item.ItemWriter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TaxonDoNothingWriter extends HibernateDaoSupport implements ItemWriter<Taxon>{

	@Override
	public void write(List<? extends Taxon> taxonlist) throws Exception {


			
		}

	}


