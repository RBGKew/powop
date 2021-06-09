package org.powo.portal.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.powo.model.Taxon;
import org.powo.model.registry.Organisation;
import org.powo.portal.view.helpers.NameHelper;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.format.DateTimeFormat;

@Slf4j
public class Identifications {

	class Identification {
		public String barcode;
		public String date;
		public String notes;
		public String thumbnail;
		public String typeStatus;
		public String url;
		public String identifiedAs;
	}

	List<Identification> identifications;
	Set<Organisation> sources;

	private Taxon taxon;
	private NameHelper helper;

	public Identifications(Taxon taxon) {
		this.taxon = taxon;
		this.helper = new NameHelper();
		this.identifications = new ArrayList<>();
		if (taxon.looksAccepted()) {
			populateIdentifications(taxon.getIdentifications());
			for (Taxon synonym : taxon.getSynonymNameUsages()) {
				populateIdentifications(synonym.getIdentifications());
			}
		}
	}

	public Collection<Identification> getIdentifications() {
		return identifications;
	}

	public int getCount() {
		return identifications.size();
	}

	public Set<Organisation> getSources() {
		if (sources == null) {
			sources = new HashSet<>();
		}
		return sources;
	}

	private void populateIdentifications(Collection<org.powo.model.Identification> idents) {
		for (org.powo.model.Identification identification : idents) {
			Identification id = new Identification();
			id.url = identification.getIdentifiedBy();
			id.notes = identification.getIdentificationReferences();
			id.thumbnail = identification.getIdentificationRemarks();
			id.typeStatus = identification.getTypeStatus();

			if (identification.getDateIdentified() != null) {
				id.date = identification.getDateIdentified().toString(DateTimeFormat.mediumDate());
			}

			id.barcode = this.extractBarcode(id.url);

			if (!taxon.equals(identification.getTaxon())) {
				id.identifiedAs = helper.taxonLinkWithoutAuthor(identification.getTaxon(), null).toString();
			}

			log.debug("Adding {} from {}", id.barcode, identification.getTaxon().getIdentifier());
			identifications.add(id);
		}
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
