package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.emonocot.model.Taxon;
import org.joda.time.format.DateTimeFormat;

public class Identifications {

	class Identification {
		public String date;
		public String barcode;
		public String url;
		public String notes;
	}

	List<Identification> identifications;

	private Taxon taxon;

	public Identifications(Taxon taxon)  {
		this.taxon = taxon;
	}

	public Collection<Identification> getIdentifications() {
		if(identifications == null) {
			this.identifications = new ArrayList<>();
			for(org.emonocot.model.Identification identification : taxon.getIdentifications()) {
				Identification id = new Identification();
				id.url = identification.getIdentificationReferences();
				id.notes = identification.getIdentificationRemarks();

				if(identification.getDateIdentified() != null) {
					id.date = identification.getDateIdentified().toString(DateTimeFormat.mediumDate());
				}
				if(id.url != null && id.url.lastIndexOf('/') != -1) {
					id.barcode = id.url.substring(id.url.lastIndexOf('/') + 1);
				}

				identifications.add(id);
			}
		}
		return identifications;
	}
	
	public int getCount() {
		return taxon.getIdentifications().size();
	}
}
