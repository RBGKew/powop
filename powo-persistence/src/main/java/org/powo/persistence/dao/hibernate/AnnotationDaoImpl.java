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

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.FetchMode;
import org.powo.model.Annotation;
import org.powo.model.constants.RecordType;
import org.powo.model.hibernate.Fetch;
import org.powo.persistence.dao.AnnotationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class AnnotationDaoImpl extends SearchableDaoImpl<Annotation> implements
AnnotationDao {

	protected JdbcTemplate jdbcTemplate;

	@Autowired
	public final void setDataSource(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		jdbcTemplate.afterPropertiesSet();
	}

	/**
	 *
	 */
	private static Map<String, Fetch[]> FETCH_PROFILES;

	static {
		FETCH_PROFILES = new HashMap<String, Fetch[]>();
		FETCH_PROFILES.put("annotated-obj", new Fetch[] {
				new Fetch("annotatedObj", FetchMode.SELECT)
		});
	}

	/**
	 *
	 */
	public AnnotationDaoImpl() {
		super(Annotation.class);
	}

	@Override
	protected boolean isSearchableObject() {
		return false;
	}

	@Override
	protected final Fetch[] getProfile(final String profile) {
		return AnnotationDaoImpl.FETCH_PROFILES.get(profile);
	}

	@Override
	public Annotation findAnnotation(RecordType recordType, Long id, Long jobId) {
		try {
			Object[] args = new Object[] {id, "Taxon", jobId};
			int[] argTypes = new int[] {Types.BIGINT, Types.VARCHAR, Types.BIGINT};
			Long annotationId = jdbcTemplate.queryForObject("Select a.id from Annotation a where a.annotatedObjId = ? and a.annotatedObjType = ? and a.jobId = ?", args, argTypes, Long.class);
			return (Annotation) getSession().load(Annotation.class, annotationId);
		} catch(IncorrectResultSizeDataAccessException irsdae) {
			if(irsdae.getActualSize() == 0) {
				return null;
			} else {
				throw irsdae;
			}
		}
	}
}
