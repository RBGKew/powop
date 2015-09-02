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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.emonocot.model.Image;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.TypeAndSpecimen;
import org.springframework.batch.item.file.FlatFileItemWriter;

public class ManyToManyWriter extends FlatFileItemWriter<Taxon> {

	private Class type;

	public void setType(Class type) {
		this.type = type;
	}

	@Override
	public void write(List<? extends Taxon> taxa) throws Exception {
		List items = new ArrayList();

		for(Taxon t : taxa) {
			Set<Taxon> taxaList = new HashSet<Taxon>();
			taxaList.add(t);
			if(type.equals(Reference.class)) {
				for(Reference r : t.getReferences()) {
					if(r.getLicense() != null && !r.getLicense().isEmpty()) {
						r.setTaxa(taxaList);
						items.add(r);
					}
				}
			} else if(type.equals(Image.class)) {
				for(Image i : t.getImages()) {
					if(i.getLicense() != null && !i.getLicense().isEmpty()) {
						i.setTaxa(taxaList);
						items.add(i);
					}
				}
			} else if(type.equals(TypeAndSpecimen.class)) {
				for(TypeAndSpecimen o : t.getTypesAndSpecimens()) {
					if(o.getLicense() != null && !o.getLicense().isEmpty()) {
						o.setTaxa(taxaList);
						items.add(o);
					}
				}
			}
		}

		super.write(items);
	}


}
