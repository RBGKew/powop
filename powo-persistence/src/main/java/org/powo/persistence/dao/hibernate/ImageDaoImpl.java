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
package org.powo.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gbif.ecat.voc.Rank;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.model.hibernate.Fetch;
import org.powo.persistence.dao.ImageDao;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDaoImpl extends DaoImpl<Image> implements ImageDao {

	private static Map<String, Fetch[]> FETCH_PROFILES;

	static {
		FETCH_PROFILES = new HashMap<String, Fetch[]>();
		FETCH_PROFILES.put("image-page", new Fetch[] {
				new Fetch("taxon", FetchMode.JOIN),
				new Fetch("authority", FetchMode.JOIN),
				new Fetch("comments", FetchMode.SELECT)
		});
		FETCH_PROFILES.put("image-taxon", new Fetch[] {
				new Fetch("taxon", FetchMode.JOIN)
		});
	}

	public ImageDaoImpl() {
		super(Image.class);
	}

	@Override
	protected final Fetch[] getProfile(final String profile) {
		return ImageDaoImpl.FETCH_PROFILES.get(profile);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Image> getTopImages(Taxon rootTaxon, int n) {
		String queryTemplate = "select img from Image as img join img.taxon as taxon where %s order by img.rating desc";
		Query q;

		List<Long> rootImgs = new ArrayList<>();
		for(Image img : rootTaxon.getImages()) {
			rootImgs.add(img.getId());
		}

		if(!rootImgs.isEmpty()) {
			queryTemplate = String.format(queryTemplate, "img.id not in (:rootImgs) and %s");
		}

		if(rootTaxon.getTaxonRank() != null && rootTaxon.getTaxonRank().equals(Rank.FAMILY)) {
			q = getSession().createQuery(String.format(queryTemplate, "taxon.family = :family"));
			q.setParameter("family", rootTaxon.getFamily());
			if(!rootImgs.isEmpty()) q.setParameterList("rootImgs", rootImgs);
		} else if (rootTaxon.getTaxonRank() != null && rootTaxon.getTaxonRank().equals(Rank.GENUS)) {
			q = getSession().createQuery(String.format(queryTemplate, "taxon.family = :family and taxon.genus = :genus"));
			q.setParameter("family", rootTaxon.getFamily());
			q.setParameter("genus", rootTaxon.getGenus());
			if(!rootImgs.isEmpty()) q.setParameterList("rootImgs", rootImgs);
		} else {
			// we don't want to add anything to species and below
			q = getSession().createQuery("from Image where 1=0");
		}

		q.setMaxResults(n);

		List<Image> ret = (List<Image>)q.list();
		if(ret != null) {
			return ret;
		} else {
			return new ArrayList<>();
		}
	}
}
