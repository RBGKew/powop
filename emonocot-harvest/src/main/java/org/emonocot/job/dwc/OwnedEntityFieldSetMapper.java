package org.emonocot.job.dwc;

import org.emonocot.api.TaxonService;
import org.emonocot.job.dwc.taxon.CannotFindRecordException;
import org.emonocot.model.OwnedEntity;
import org.emonocot.model.Taxon;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;

public class OwnedEntityFieldSetMapper<T extends OwnedEntity> extends BaseDataFieldSetMapper<T> {
	
	private Logger logger = LoggerFactory.getLogger(OwnedEntityFieldSetMapper.class);
	
	private TaxonService taxonService;
	
	@Autowired
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

	public OwnedEntityFieldSetMapper(Class<T> newType) {
		super(newType);		
	}
	
	@Override
	public void mapField(T object, String fieldName, String value)
			throws BindException {
		super.mapField(object, fieldName, value);
		
		ConceptTerm term = getTermFactory().findTerm(fieldName);
		
		// DwcTerms
        if (term instanceof DwcTerm) {
            DwcTerm dwcTerm = (DwcTerm) term;
            switch (dwcTerm) {
            case taxonID:
				if (value != null || value.trim().length() > 0) {
					Taxon taxon = taxonService.find(value);
					if (taxon == null) {
						logger.error("Cannot find record " + value);
						throw new CannotFindRecordException(value);
					} else {
						taxon = new Taxon();
						taxon.setIdentifier(value);
						object.setTaxon(taxon);
					}
				}
                break;
            default:
                break;
            }
        }
	}

}
