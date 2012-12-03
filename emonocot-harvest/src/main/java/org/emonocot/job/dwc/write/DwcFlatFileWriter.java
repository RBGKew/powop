package org.emonocot.job.dwc.write;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.emonocot.model.Image;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.TypeAndSpecimen;
import org.springframework.batch.item.file.FlatFileItemWriter;

public class DwcFlatFileWriter extends FlatFileItemWriter<Taxon> {
	
	private String extension;
	
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	@Override
	public void write(List<? extends Taxon> taxa) throws Exception {
		
		if(extension.equals("Taxon")) {
		    super.write(taxa);
		} else {
			List items = new ArrayList();
			for(Taxon t : taxa) {
				Set<Taxon> taxaSet = new HashSet<Taxon>();
				taxaSet.add(t);
			    switch(extension) {
			    case "Distribution":
			    	items.addAll(t.getDistribution());
			        break;
			    case "Description":
			    	items.addAll(t.getDescriptions());
			    	break;
			    case "Identifier":
			    	items.addAll(t.getIdentifiers());
			    	break;
			    case "MeasurementOrFact":
			    	items.addAll(t.getMeasurementsOrFacts());
			    	break;
			    case "VernacularName":
			    	items.addAll(t.getVernacularNames());
			    	break;
			    case "Image":
			    	for(Image image : t.getImages()) {
			    		image.setTaxa(taxaSet);
			    		items.add(image);
			    	}
			    	break;
			    case "TypeAndSpecimen":
			    	for(TypeAndSpecimen typeAndSpecimen : t.getTypesAndSpecimens()) {
			    		typeAndSpecimen.setTaxa(taxaSet);
			    		items.add(typeAndSpecimen);
			    	}
			    	break;
			    case "Reference":
			    	for(Reference reference : t.getReferences()) {
			    		reference.setTaxa(taxaSet);
			    		items.add(reference);
			    	}
			    	break;
			    }
			}
			super.write(items);
		}
	}

}
