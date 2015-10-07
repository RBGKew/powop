package org.emonocot.job.dwc.identification;

import org.emonocot.api.job.TermFactory;
import org.emonocot.job.dwc.read.OwnedEntityFieldSetMapper;
import org.emonocot.model.Identification;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.Term;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

public class FieldSetMapper extends OwnedEntityFieldSetMapper<Identification> {

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

	public FieldSetMapper() {
		super(Identification.class);
	}

	@Override
	public final void mapField(final Identification identification, final String fieldName,
			final String value) throws BindException {
		super.mapField(identification, fieldName, value);
		Term term = TermFactory.findTerm(fieldName);
		if (term instanceof DwcTerm) {
			DwcTerm dwcTerm = (DwcTerm) term;
			switch(dwcTerm) {
			case identificationQualifier:
				identification.setIdentificationQualifier(value);
				break;
			case typeStatus:
				identification.setTypeStatus(value);
				break;
			case identifiedBy:
				identification.setIdentifiedBy(value);
				break;
			case dateIdentified:
				identification.setDateIdentified(conversionService.convert(value, DateTime.class));
				break;
			case identificationReferences:
				identification.setIdentificationReferences(value);
				break;
			case identificationVerificationStatus:
				identification.setIdentificationVerificationStatus(value);
				break;
			case identificationRemarks:
				identification.setIdentificationRemarks(value);
				break;
			case identificationID:
				identification.setIdentifier(value);
				break;
			default:
				break;
			}
		}
	}
}