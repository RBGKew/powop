package org.powo.job.dwc.taxon;

import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.Term;
import org.powo.api.job.TermFactory;
import org.powo.job.dwc.read.BaseDataFieldSetMapper;
import org.powo.model.Taxon;
import org.springframework.validation.BindException;

/**
 * Mapper which only maps associations. For use with the Linking Processor
 */
public class LinkingFieldSetMapper extends BaseDataFieldSetMapper<Taxon> {

	public LinkingFieldSetMapper() {
		super(Taxon.class);
	}

	@Override
	public final void mapField(final Taxon object, final String fieldName, final String value) throws BindException {
		super.mapField(object, fieldName, value);
		Term term = TermFactory.findTerm(fieldName);

		if (term instanceof DwcTerm) {
			DwcTerm dwcTerm = (DwcTerm) term;
			switch (dwcTerm) {
			case acceptedNameUsageID:
				if (value != null && value.trim().length() != 0) {
					if(object.getAcceptedNameUsage() == null) {
						Taxon taxon = new Taxon();
						object.setAcceptedNameUsage(taxon);
					}
					object.getAcceptedNameUsage().setIdentifier(value);
				}
				break;
			case acceptedNameUsage:
				if (value != null && value.trim().length() != 0) {
					if(object.getAcceptedNameUsage() == null) {
						Taxon taxon = new Taxon();
						object.setAcceptedNameUsage(taxon);
					}
					object.getAcceptedNameUsage().setScientificName(value);
				}
				break;
			case originalNameUsageID:
				if (value != null && value.trim().length() != 0) {
					if(object.getOriginalNameUsage() == null) {
						Taxon taxon = new Taxon();
						object.setOriginalNameUsage(taxon);
					}
					object.getOriginalNameUsage().setIdentifier(value);

				}
				break;
			case originalNameUsage:
				if (value != null && value.trim().length() != 0) {
					if(object.getOriginalNameUsage() == null) {
						Taxon taxon = new Taxon();
						object.setOriginalNameUsage(taxon);
					}
					object.getOriginalNameUsage().setScientificName(value);
				}
				break;
			case parentNameUsageID:
				if (value != null && value.trim().length() != 0) {
					if(object.getParentNameUsage() == null) {
						Taxon taxon = new Taxon();
						object.setParentNameUsage(taxon);
					}
					object.getParentNameUsage().setIdentifier(value);
				}
				break;
			case parentNameUsage:
				if (value != null && value.trim().length() != 0) {
					if(object.getParentNameUsage() == null) {
						Taxon taxon = new Taxon();
						object.setParentNameUsage(taxon);
					}
					object.getParentNameUsage().setScientificName(value);
				}
				break;
			default:
				break;
			}
		}
	}
}
