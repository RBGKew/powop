package org.emonocot.service;

import org.emonocot.model.taxon.Taxon;

public interface TaxonService extends Service<Taxon> {

	boolean verify(String identifer, String scientificName);

}