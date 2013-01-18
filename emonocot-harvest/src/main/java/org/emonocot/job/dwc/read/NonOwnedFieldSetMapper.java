package org.emonocot.job.dwc.read;

import org.emonocot.api.TaxonService;
import org.emonocot.job.dwc.exception.CannotFindRecordException;
import org.emonocot.model.BaseData;
import org.emonocot.model.NonOwned;
import org.emonocot.model.Taxon;
import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;

public class NonOwnedFieldSetMapper<T extends BaseData> extends BaseDataFieldSetMapper<T> {
	
	private Logger logger = LoggerFactory.getLogger(NonOwnedFieldSetMapper.class);
	
	private TaxonService taxonService;

	public NonOwnedFieldSetMapper(Class<T> newType) {
		super(newType);
	}
	
	@Autowired
	public final void setTaxonService(final TaxonService newTaxonService) {
	       this.taxonService = newTaxonService;
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
            	Taxon taxon = taxonService.find(value);
                if (taxon == null) {
                    // Non-owned objects can exist without a taxon explicitly set
                } else {
                    ((NonOwned)object).getTaxa().add(taxon);
                }
                break;
            default:
                break;
            }
        }
	}
}
