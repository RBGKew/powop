package org.powo.job.dwc.taxon;

import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.Term;
import org.powo.api.job.TermFactory;
import org.powo.job.dwc.read.BaseDataFieldSetMapper;
import org.powo.model.Taxon;
import org.springframework.validation.BindException;

/**
 * Mapper which only maps the taxon identifier. Used with Skipping processor
 */
public class SkippingFieldSetMapper extends BaseDataFieldSetMapper<Taxon>  {

	public SkippingFieldSetMapper() {
		super(Taxon.class);
	}

	@Override
	public final void mapField(final Taxon object, final String fieldName, final String value) throws BindException {
		super.mapField(object, fieldName, value);
		Term term = TermFactory.findTerm(fieldName);

		if (term instanceof DwcTerm && DwcTerm.taxonID.equals(term)) {
			object.setIdentifier(value);
		}
	}
}
