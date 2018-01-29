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
package org.powo.job.dwc.read;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.powo.api.job.TermFactory;
import org.powo.model.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 * @param <T> the type of object which this class maps
 */
public abstract class DarwinCoreFieldSetMapper<T extends Base> implements FieldSetMapper<T> {

	private Logger logger = LoggerFactory.getLogger(DarwinCoreFieldSetMapper.class);

	private String[] fieldNames  = new String[0];

	private Map<String, String> defaultValues = new HashMap<String,String>();

	private Class<T> type;

	/**
	 *
	 * @param object the object to map onto
	 * @param fieldName the name of the field
	 * @param value the value to map
	 * @throws BindException if there is a problem mapping
	 *         the value to the object
	 */
	public abstract void mapField(T object, String fieldName, String value) throws BindException;

	/**
	 *
	 */
	private TermFactory termFactory = new TermFactory();

	/**
	 *
	 * @param newFieldNames Set the names of the fields
	 */
	public void setFieldNames(String[] newFieldNames) {
		this.fieldNames = newFieldNames;
	}

	/**
	 *
	 * @param newDefaultValues Set the defaultValues of the fields
	 */
	public void setDefaultValues(Map<String, String> newDefaultValues) {
		this.defaultValues = newDefaultValues;
	}

	/**
	 *
	 * @param newType Set the type
	 */
	public DarwinCoreFieldSetMapper(Class<T> newType) {
		this.type = newType;
	}

	/**
	 * @param fieldSet Set the field set
	 * @return an object
	 * @throws BindException if there is a problem binding
	 *         the values to the object
	 */
	public T mapFieldSet(FieldSet fieldSet) throws BindException {
		T t;
		try {
			t = type.newInstance();
		} catch (InstantiationException e) {
			logger.info("Unable to create an instance of " + type, e);
			BindException be = new BindException(this, "target");
			be.reject("could not instantiate", e.getMessage());
			throw be;
		} catch (IllegalAccessException e) {
			logger.info("Unable to create an instance of " + type, e);
			BindException be = new BindException(this, "target");
			be.reject("could not instantiate", e.getMessage());
			throw be;
		}
		logger.debug("Mapping object " + t + " with fieldNames " + Arrays.toString(fieldNames) + " and fieldSet " + fieldSet);
		try {
			// Default values go first, specific values override
			logger.trace("Mapping default values");
			for (String defaultTerm : defaultValues.keySet()) {
				mapField(t, defaultTerm, defaultValues.get(defaultTerm));
			}

			logger.trace("Mapping fields");
			for (int i = 0; i < fieldNames.length; i++) {
				if(fieldNames[i].indexOf(" ") != -1) {
					String fieldName = fieldNames[i].substring(0, fieldNames[i].indexOf(" "));
					String defaultValue = fieldNames[i].substring(fieldNames[i].indexOf(" ") + 1);
					String value = fieldSet.readString(i);
					if(value.isEmpty()) {
						mapField(t, fieldName, defaultValue);
					} else {
						mapField(t, fieldName, value);
					}
				} else {
					mapField(t, fieldNames[i], fieldSet.readString(i));
				}
			}

		} catch (BindException e) {
			logger.error(e.getMessage());
			throw e;
		}
		logger.trace("Returning object " + t);
		return t;
	}

	/**
	 *
	 * @return a term factory instance
	 */
	public TermFactory getTermFactory() {
		return termFactory;
	}
}
