package org.powo.portal.json.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.powo.portal.view.Distributions;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;

import lombok.Getter;

import org.powo.model.Taxon;
import org.powo.model.constants.TaxonField;
import static org.powo.model.constants.TaxonField.*;

public class TaxonResponse {

	private Taxon taxon;
	private List<TaxonField> outputFields;
	private ObjectMapper mapper = new ObjectMapper();

	@Getter
	private Map<String, Object> output;

	public TaxonResponse(Taxon taxon, List<TaxonField> outputFields) {
		this.taxon = taxon;
		this.outputFields = outputFields != null ? outputFields : new ArrayList<>(0);
		buildOutput();
	}

	public TaxonResponse(Taxon taxon) {
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
		case descriptions:
			Descriptions desc = new Descriptions(taxon);
			if (!desc.isEmpty()) {
				output.put(descriptions.toString(), desc);
			}
			break;
		case distribution:
			if (!taxon.getDistribution().isEmpty()) {
				output.put(distribution.toString(), new Distributions(taxon));
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
