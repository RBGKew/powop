package org.emonocot.job.dwc.typeandspecimen;

import org.emonocot.api.TypeAndSpecimenService;
import org.emonocot.job.dwc.read.NonOwnedProcessor;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class Processor extends NonOwnedProcessor<TypeAndSpecimen, TypeAndSpecimenService> {

    private Logger logger = LoggerFactory.getLogger(Processor.class);
    
    @Autowired
    public void setTypeAndSpecimenService(TypeAndSpecimenService service) {
    	super.service = service;
    }

	@Override
	protected void doUpdate(TypeAndSpecimen persisted, TypeAndSpecimen t) {
		persisted.setBibliographicCitation(t.getBibliographicCitation());
		persisted.setCatalogNumber(t.getCatalogNumber());
		persisted.setCollectionCode(t.getCollectionCode());
		persisted.setDecimalLatitude(t.getDecimalLatitude());
		persisted.setDecimalLongitude(t.getDecimalLongitude());
		persisted.setInstitutionCode(t.getInstitutionCode());
		persisted.setLocality(t.getLocality());
		persisted.setRecordedBy(t.getRecordedBy());
		persisted.setScientificName(t.getScientificName());
		persisted.setSex(t.getSex());
		persisted.setSource(t.getSource());
		persisted.setTaxonRank(t.getTaxonRank());
		persisted.setTypeDesignatedBy(t.getTypeDesignatedBy());
		persisted.setTypeDesignationType(t.getTypeDesignationType());
		persisted.setTypeStatus(t.getTypeStatus());
		persisted.setVerbatimEventDate(t.getVerbatimEventDate());
		persisted.setVerbatimLabel(t.getVerbatimLabel());
		persisted.setVerbatimLatitude(t.getVerbatimLatitude());
		persisted.setVerbatimLongitude(t.getVerbatimLongitude());

	}

	@Override
	protected void doPersist(TypeAndSpecimen t) {
		
	}

	@Override
	protected RecordType getRecordType() {
		return RecordType.TypeAndSpecimen;
	}

	@Override
	protected void bind(TypeAndSpecimen t) {
		if (t.getIdentifier() != null) {
            boundObjects.put(t.getIdentifier(), t);
        }
        if (t.getCatalogNumber() != null) {
            boundObjects.put(t.getCatalogNumber(), t);
        }
	}

	@Override
	protected TypeAndSpecimen retrieveBound(TypeAndSpecimen t) {
		if (t.getIdentifier() != null) {
            return service.find(t.getIdentifier());
        } else if (t.getCatalogNumber() != null) {
            return service.findByCatalogNumber(t.getCatalogNumber());
        }
        return null;
	}

	@Override
	protected TypeAndSpecimen lookupBound(TypeAndSpecimen t) {
		if (t.getIdentifier() != null) {
            return boundObjects.get(t.getIdentifier());
        } else if (t.getCatalogNumber() != null) {
            return boundObjects.get(t.getCatalogNumber());
        }
        return null;
	}

	@Override
	protected void doValidate(TypeAndSpecimen t) throws Exception {
		
	}
}
