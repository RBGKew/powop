package org.powo.portal.view;

import org.apache.commons.lang3.ArrayUtils;
import org.powo.model.Description;
import org.powo.model.Taxon;
import org.powo.model.constants.DescriptionType;
import org.powo.model.constants.TaxonomicStatus;
import org.springframework.context.MessageSource;

public class Summary {

	private Taxon taxon;

	private MessageSource messageSource;

	public Summary(Taxon taxon, MessageSource messageSource) {
		this.taxon = taxon;
		this.messageSource = messageSource;
	}

	private String existingDescription() {
		for (Description description : taxon.getDescriptions()) {
			if (description.getTypes().contains(DescriptionType.summary)) {
				return description.getDescription();
			}
		}
		return null;
	}

	private String taxonRank() {
		if (taxon.getTaxonRank() != null) {
			return taxon.getTaxonRank().toString().toLowerCase();
		}
		return "plant";
	}

	public String taxonStatus() {
		if (taxon.getTaxonomicStatus() == null) {
			return null;
		} else {
			switch (taxon.getTaxonomicStatus()) {
			case Synonym:
			case Heterotypic_Synonym:
			case Homotypic_Synonym:
			case DeterminationSynonym:
			case IntermediateRankSynonym:
			case Proparte_Synonym:
				return "synonym";
			case Accepted:
				return "accepted";
			case Artificial_Hybrid:
				return "an artifical hybrid";
			case Doubtful:
			case Misapplied:
				return "misapplied name";
			case Unplaced:
				return "an unplaced name";
			default:
				return null;
			}
		}
	}

	private String createAcceptedNameSummary() {
		if (existingDescription() != null) {
			return existingDescription();
		}
		String[] elements = {
				taxonRank(),
				taxonStatus(),
				new SummaryDistribution(taxon, messageSource).build(),
				new SummaryUses().build(taxon, messageSource)
		};

		elements = ArrayUtils.removeAllOccurences(elements, null);
		elements = ArrayUtils.removeAllOccurences(elements, "");
		if (elements.length < 2) {
			return null;
		}
		String descriptionString = String.format("This %s is %s", elements[0], elements[1]);
		if (elements.length > 2) {
			descriptionString += String.format(", and %s", elements[2]);
		}
		if (elements.length > 3) {
			descriptionString += String.format(". It is %s", elements[3]);
		}
		return descriptionString += ".";
	}

	private String createSynonymNameSummary() {
		String string = String.format("This is a %s", taxonStatus());
		if (taxon.getAcceptedNameUsage() != null) {
			string += " of ";
		}
		return string;
	}

	private String createUnplacedNameSummary() {
		return "This is an unplaced name.";
	}

	public String build() {
		if (taxon.isAccepted() || taxon.getTaxonomicStatus() == TaxonomicStatus.Artificial_Hybrid) {
			return createAcceptedNameSummary();
		}
		if (taxon.isSynonym() || taxon.getTaxonomicStatus() == TaxonomicStatus.Misapplied) {
			return createSynonymNameSummary();
		}
		if (TaxonomicStatus.Unplaced.equals(taxon.getTaxonomicStatus())) {
			return createUnplacedNameSummary();
		}
		return "";
	}
}
