package org.powo.portal.json.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.powo.portal.view.Distributions;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import lombok.Getter;

import org.powo.model.Distribution;
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
				output.put("distributionEnvelope", getDistributionEnvelope());
			}
			break;
		default:
			break;
		}
	}

	public Coordinate[] getDistributionEnvelope() {
		if (taxon == null) {
			return null;
		}

		List<Geometry> list = new ArrayList<Geometry>(taxon.getDistribution().size());
		for (Distribution d : taxon.getDistribution()) {
			list.add(d.getLocation().getEnvelope());
		}

		GeometryCollection geometryCollection = new GeometryCollection(list.toArray(new Geometry[list.size()]), new GeometryFactory());
		return geometryCollection.getEnvelope().getCoordinates();
	}


	public String toString() {
		return Joiner.on("\r").withKeyValueSeparator("=").join(output);
	}
}
