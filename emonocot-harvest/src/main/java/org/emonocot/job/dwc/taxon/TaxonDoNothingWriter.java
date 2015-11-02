package org.emonocot.job.dwc.taxon;

import java.util.List;

import org.emonocot.model.Taxon;
import org.springframework.batch.item.ItemWriter;

public class TaxonDoNothingWriter implements ItemWriter<Taxon>{

	@Override
	public void write(List<? extends Taxon> taxonlist){		
		}

	}


