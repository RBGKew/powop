package org.emonocot.service.impl;

import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
import org.springframework.stereotype.Service;

@Service
public class TaxonServiceImpl implements TaxonService {

	@Override
	public Taxon load(String identifer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean verify(String identifer, String scientificName) {
		// TODO Auto-generated method stub
		return false;
	}

}
