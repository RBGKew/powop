package org.emonocot.job.dwc.write;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.emonocot.model.BaseData;
import org.emonocot.model.Description;
import org.emonocot.model.Distribution;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.VernacularName;
import org.emonocot.model.Identifier;
import org.emonocot.model.Taxon;
import org.springframework.batch.item.file.FlatFileItemWriter;

public class OneToManyWriter extends FlatFileItemWriter<Taxon> {
    
    private Class type;
    
    public void setType(Class type) {
		this.type = type;
	}

    @Override
    public void write(List<? extends Taxon> taxa) throws Exception {
		List items = new ArrayList();
		for(Taxon t : taxa) {
			if(type.equals(Description.class)) {
				addItems(items, t.getDescriptions());
			} else if(type.equals(Distribution.class)) {
				addItems(items, t.getDistribution());				
			} else if(type.equals(MeasurementOrFact.class)) {
				addItems(items, t.getMeasurementsOrFacts());
			} else if(type.equals(VernacularName.class)) {
				addItems(items, t.getVernacularNames());
			} else if(type.equals(Identifier.class)) {
				addItems(items, t.getIdentifiers());
			}
		}
		
		super.write(items);	
    }

	private void addItems(List items, Set<? extends BaseData> baseDatas) {
		for(BaseData baseData : baseDatas) {
			if(baseData.getLicense() != null && !baseData.getLicense().isEmpty()) {
				items.add(baseData);
			}
		}
	}
}
