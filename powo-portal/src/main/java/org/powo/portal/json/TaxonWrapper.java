package org.powo.portal.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.powo.portal.view.Bibliography;
import org.powo.portal.view.Descriptions;
import org.powo.portal.view.Distributions;
import org.powo.portal.view.Identifications;
import org.powo.portal.view.Images;
import org.powo.portal.view.Sources;
import org.powo.portal.view.VernacularNames;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;

import lombok.Getter;

import org.powo.model.Taxon;
import org.powo.model.constants.TaxonField;
import static org.powo.model.constants.TaxonField.*;

public class TaxonWrapper {

	private Taxon taxon;
	private List<TaxonField> outputFields;
	private ObjectMapper mapper = new ObjectMapper();

	@Getter
	private Map<String, Object> output;

	public TaxonWrapper(Taxon taxon, List<TaxonField> outputFields) {
		this.taxon = taxon;
		this.outputFields = outputFields != null ? outputFields : new ArrayList<>(0);
		buildOutput();
	}

	public TaxonWrapper(Taxon taxon) {
		this(taxon, new ArrayList<>(0));
	}

	private void buildOutput() {
		output = mapper.convertValue(taxon, new TypeReference<Map<String, Object>>(){});
		for(TaxonField field : outputFields) {
			prepareField(field);
		}
	}

	private void prepareField(TaxonField field) {
		switch(field) {
		case source: 
			output.put(source.toString(), new Sources(taxon));
			break;
		case bibliography:
			Bibliography bib = new Bibliography(taxon);
			if (!bib.isEmpty()) {
				output.put(bibliography.toString(), bib);
			}
			break;
		case descriptions:
			Descriptions desc = new Descriptions(taxon);
			if (!desc.getBySource().isEmpty()) {
				output.put(descriptions.toString(), desc);
			}
			break;
		case distribution:
			if (!taxon.getDistribution().isEmpty()) {
				output.put(distribution.toString(), new Distributions(taxon));
			}
			break;
		case identifications:
			if (!taxon.getIdentifications().isEmpty()) {
				output.put(identifications.toString(), new Identifications(taxon));
			}
			break;
//		case images:
//			Images img = new Images(taxon, imageService);
//			if (!img.getAll().isEmpty()) {
//				output.put(images.toString(), img);
//			}
//			break;
		case uses:
			Descriptions use = new Descriptions(taxon, true);
			if (!use.getBySource().isEmpty()) {
				output.put(uses.toString(), use);
			}
			break;
		case vernacularNames:
			if (!taxon.getVernacularNames().isEmpty()) {
				output.put(vernacularNames.toString(), new VernacularNames(taxon));
			}
			break;
		default:
			break;
		}
	}
	
	public String toString() {
		return Joiner.on("\r").withKeyValueSeparator("=").join(output);
	}
}
