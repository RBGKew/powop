package org.powo.portal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.powo.model.Identification;
import org.powo.model.Taxon;
import org.powo.portal.view.IdentificationView;
import org.powo.portal.view.helpers.NameHelper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class IdentificationsViewService {
	private final NameHelper helper;

	public List<IdentificationView> getIdentifications(Taxon taxon) {
		var identifications = new ArrayList<IdentificationView>();

		if (taxon.looksAccepted()) {
			identifications.addAll(populateIdentifications(taxon, taxon.getIdentifications()));
			for (Taxon synonym : taxon.getSynonymNameUsages()) {
				identifications.addAll(populateIdentifications(taxon, synonym.getIdentifications()));
			}
		}

		return identifications;
	}

	private List<IdentificationView> populateIdentifications(Taxon taxon, Collection<Identification> idents) {
		var ids = new ArrayList<IdentificationView>();

		for (var identification : idents) {
			var id = taxon.equals(identification.getTaxon())
				? new IdentificationView(identification)
				: new IdentificationView(
						identification, 
						helper.taxonLinkWithoutAuthor(identification.getTaxon(), null).toString()
					);
			
			log.debug("Adding {} from {}", id.getBarcode(), identification.getTaxon().getIdentifier());
			ids.add(id);
		}

		return ids;
	}
}
