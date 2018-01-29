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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.powo.model.Taxon;
import org.powo.model.hibernate.Fetch;
import org.powo.pager.DefaultPageImpl;
import org.powo.pager.Page;
import org.powo.persistence.dao.TaxonDao;
import org.springframework.stereotype.Repository;

@Repository
public class TaxonDaoImpl extends DaoImpl<Taxon> implements TaxonDao {

	private static Map<String, Fetch[]> FETCH_PROFILES;

	static {
		FETCH_PROFILES = new HashMap<String, Fetch[]>();
		FETCH_PROFILES.put("taxon-with-children", new Fetch[] {new Fetch(
				"childNameUsages", FetchMode.SELECT)});
		FETCH_PROFILES.put("classification-tree", new Fetch[] {
				new Fetch("childNameUsages", FetchMode.SELECT)});
		FETCH_PROFILES.put("taxon-with-annotations", new Fetch[] {new Fetch(
				"annotations", FetchMode.SELECT)});
		FETCH_PROFILES.put("taxon-with-related", new Fetch[] {
				new Fetch("parentNameUsage", FetchMode.JOIN),
				new Fetch("acceptedNameUsage", FetchMode.JOIN),
				new Fetch("childNameUsages", FetchMode.SELECT),
				new Fetch("synonymNameUsages", FetchMode.SELECT),
				new Fetch("annotations", FetchMode.SELECT)});
		FETCH_PROFILES.put("taxon-page", new Fetch[] {
				new Fetch("parentNameUsage", FetchMode.JOIN),
				new Fetch("parentNameUsage.authority", FetchMode.SELECT),
				new Fetch("higherClassification", FetchMode.SELECT),
				new Fetch("higherClassification.authority", FetchMode.SELECT),
				new Fetch("acceptedNameUsage", FetchMode.JOIN),
				new Fetch("acceptedNameUsage.authority", FetchMode.SELECT),
				new Fetch("childNameUsages", FetchMode.SELECT),
				new Fetch("childNameUsages.authority", FetchMode.SELECT),
				new Fetch("synonymNameUsages", FetchMode.SELECT),
				new Fetch("synonymNameUsages.authority", FetchMode.SELECT),
				new Fetch("distribution", FetchMode.SELECT),
				new Fetch("distribution.authority", FetchMode.SELECT),
				new Fetch("distribution.references", FetchMode.SELECT),
				new Fetch("distribution.references.authority", FetchMode.SELECT),
				new Fetch("descriptions", FetchMode.SELECT),
				new Fetch("descriptions.authority", FetchMode.SELECT),
				new Fetch("descriptions.type", FetchMode.SELECT),
				new Fetch("descriptions.references", FetchMode.SELECT),
				new Fetch("descriptions.references.authority", FetchMode.SELECT),
				new Fetch("images", FetchMode.SELECT),
				new Fetch("images.authority", FetchMode.SELECT),
				new Fetch("namePublishedIn", FetchMode.JOIN),
				new Fetch("namePublishedIn.authority", FetchMode.SELECT),
				new Fetch("references", FetchMode.SELECT),
				new Fetch("references.authority", FetchMode.SELECT),
				new Fetch("authority", FetchMode.JOIN),
				new Fetch("identifiers", FetchMode.SELECT),
				new Fetch("identifiers.authority", FetchMode.SELECT),
				new Fetch("vernacularNames", FetchMode.SELECT),
				new Fetch("vernacularNames.authority", FetchMode.SELECT),
				new Fetch("measurementsOrFacts", FetchMode.SELECT),
				new Fetch("measurementsOrFacts.authority", FetchMode.SELECT),
				new Fetch("trees", FetchMode.SELECT),
				new Fetch("trees.authority", FetchMode.SELECT),
				new Fetch("uri", FetchMode.SELECT),
				new Fetch("typesAndSpecimens", FetchMode.SELECT),
				new Fetch("typesAndSpecimens.authority", FetchMode.SELECT)});
		/* */
		FETCH_PROFILES.put("object-page", FETCH_PROFILES.get("taxon-page"));
		/* */
		FETCH_PROFILES.put("taxon-with-image", new Fetch[] {new Fetch("image",
				FetchMode.SELECT)});
		FETCH_PROFILES.put("taxon-with-content", new Fetch[] {
				new Fetch("descriptions", FetchMode.SELECT),
				new Fetch("sources", FetchMode.SELECT)});
		FETCH_PROFILES.put("taxon-ws", new Fetch[] {
				new Fetch("parentNameUsage", FetchMode.JOIN),
				new Fetch("acceptedNameUsage", FetchMode.JOIN),
				new Fetch("childNameUsages", FetchMode.SELECT),
				new Fetch("synonymNameUsages", FetchMode.SELECT),
				/**
				 *  ISSUE http://build.e-monocot.org/bugzilla/show_bug.cgi?id=180
				 *
                new Fetch("distribution", FetchMode.SELECT),*/
				new Fetch("namePublishedIn", FetchMode.JOIN)});
	}

	/**
	 * The rank held by the the root(s) of the taxonomic classification.
	 */
	private Rank rootRank;

	public final void setRootRank(final String rank) {
		this.rootRank = Rank.valueOf(rank);
	}

	public TaxonDaoImpl() {
		super(Taxon.class);
	}

	@Override
	protected final Fetch[] getProfile(final String profile) {
		return TaxonDaoImpl.FETCH_PROFILES.get(profile);
	}

	@Override
	public final void enableProfilePostQuery(final Taxon t, final String fetch) {
		if (fetch != null && t != null) {
			for (Fetch f : getProfile(fetch)) {
				if (f.getMode().equals(FetchMode.SELECT)) {
					String association = f.getAssociation();
					if (association.indexOf(".") == -1) {
						initializeProperty(t, f.getAssociation());
					} else {
						List<String> associations = Arrays.asList(association
								.split("\\."));
						initializeProperties(t, associations);
					}
				}
			}
		}
	}

	/**
	 * Returns the child taxa of a given taxon.
	 *
	 * @param identifier
	 *            set the identifier
	 * @param pageSize
	 *            The maximum number of results to return
	 * @param pageNumber
	 *            The offset (in pageSize chunks, 0-based) from the beginning of
	 *            the recordset
	 * @param fetch
	 *            Set the fetch profile
	 * @return a Page from the resultset
	 */
	public final List<Taxon> loadChildren(final String identifier,
			final Integer pageSize, final Integer pageNumber, final String fetch) {
		Criteria criteria = getSession().createCriteria(Taxon.class);
		if (identifier == null) {
			// return the root taxa
			criteria.add(Restrictions.isNull("parentNameUsage"));
			criteria.add(Restrictions.isNotNull("scientificName"));
			criteria.add(Restrictions.eq("taxonomicStatus", TaxonomicStatus.Accepted));
			if (rootRank != null) {
				criteria.add(Restrictions.eq("taxonRank", rootRank));
			}
		} else {
			criteria.createAlias("parentNameUsage", "p");
			criteria.add(Restrictions.eq("p.identifier", identifier));
		}

		if (pageSize != null) {
			criteria.setMaxResults(pageSize);
			if (pageNumber != null) {
				criteria.setFirstResult(pageSize * pageNumber);
			}
		}
		criteria.addOrder(Order.asc("scientificName"));
		enableProfilePreQuery(criteria, fetch);
		List<Taxon> results = (List<Taxon>) criteria.list();

		for (Taxon t : results) {
			enableProfilePostQuery(t, fetch);
		}
		return results;
	}

	public Page<Taxon> searchByExample(Taxon example, boolean ignoreCase, boolean useLike) {
		Example criterion = Example.create(example);
		if(ignoreCase) {
			criterion.ignoreCase();
		}
		if(useLike) {
			criterion.enableLike();
		}
		Criteria criteria = getSession().createCriteria(Taxon.class);
		criteria.add(criterion);
		List<Taxon> results = (List<Taxon>) criteria.list();
		Page<Taxon> page = new DefaultPageImpl<Taxon>(results, 0, results.size());
		return page;
	}


}
