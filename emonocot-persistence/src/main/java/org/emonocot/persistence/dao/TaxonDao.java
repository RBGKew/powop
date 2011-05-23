package org.emonocot.persistence.dao;

import org.emonocot.model.taxon.Taxon;

public interface TaxonDao extends Dao<Taxon> {
    
    boolean verify(String identifer, String scientificName);

}
