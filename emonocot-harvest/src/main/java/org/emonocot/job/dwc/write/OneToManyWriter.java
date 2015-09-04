/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.job.dwc.write;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.emonocot.model.BaseData;
import org.emonocot.model.Description;
import org.emonocot.model.Distribution;
import org.emonocot.model.Identifier;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.Taxon;
import org.emonocot.model.VernacularName;
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
