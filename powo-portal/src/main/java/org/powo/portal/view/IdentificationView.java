package org.powo.portal.view;

import org.joda.time.format.DateTimeFormat;
import org.powo.model.Identification;
import org.springframework.util.StringUtils;

import lombok.Data;

@Data
public class IdentificationView {
	private String barcode;
	private String date;
	private String notes;
	private boolean hasImage;
	private String typeStatus;
	private String url;
	private String identifiedAs;

	public IdentificationView(Identification identification, String identifiedAs) {
		this.url = identification.getIdentifiedBy();
		this.notes = identification.getIdentificationReferences();
		this.hasImage = StringUtils.hasText(identification.getIdentificationRemarks());
		this.typeStatus = identification.getTypeStatus();
		this.identifiedAs = identifiedAs;
		this.barcode = this.extractBarcode(identification.getIdentifiedBy());

		if (identification.getDateIdentified() != null) {
			this.date = identification.getDateIdentified().toString(DateTimeFormat.mediumDate());
		}
	}

	public IdentificationView(Identification identification) {
		this(identification, null);
	}

	private String extractBarcode(String identificationUrl) {
		if (identificationUrl == null) {
			return null;
		}
		if (identificationUrl.lastIndexOf("imi=") != -1) {
			// IMI barcode
			return "IMI " + identificationUrl.substring(identificationUrl.lastIndexOf("imi=") + 4);
		}
		if (identificationUrl.lastIndexOf('/') != -1) {
			// Herbcat and Herbtrack barcodes
			return identificationUrl.substring(identificationUrl.lastIndexOf('/') + 1);
		}
		return null;
	}
}
