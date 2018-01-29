package org.powo.portal.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.powo.model.Taxon;
import org.joda.time.format.DateTimeFormat;

public class Identifications {

	class Identification {
		public String barcode;
		public String date;
		public String notes;
		public String thumbnail;
		public String typeStatus;
		public String url;
	}

	List<Identification> identifications;

	private Taxon taxon;

	public Identifications(Taxon taxon)  {
		this.taxon = taxon;
	}

	public Collection<Identification> getIdentifications() {
		if(identifications == null) {
			this.identifications = new ArrayList<>();
			for(org.powo.model.Identification identification : taxon.getIdentifications()) {
				Identification id = new Identification();
				id.url = identification.getIdentifiedBy();
				id.notes = identification.getIdentificationReferences();
				id.thumbnail = identification.getIdentificationRemarks();
				id.typeStatus = identification.getTypeStatus();

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
