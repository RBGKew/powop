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
package org.emonocot.model.marshall.json.hibernate;


import java.lang.annotation.Annotation;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;

/**
 * Simple {@link org.codehaus.jackson.map.AnnotationIntrospector} that adds support for using
 * {@link Transient} to denote ignorable fields (alongside with Jackson
 * and/or JAXB annotations).
 */
public class HibernateAnnotationIntrospector extends NopAnnotationIntrospector
{
	/**
	 * Whether we should check for existence of @Transient or not.
	 * Default value is 'true'.
	 */
	protected boolean _cfgCheckTransient;

	/*
    /**********************************************************************
    /* Construction, configuration
    /**********************************************************************
	 */

	public HibernateAnnotationIntrospector() { }

	/**
	 * Method to call to specify whether @Transient annotation is to be
	 * supported; if false, will be ignored, if true, will be used to
	 * detect "ignorable" properties.
	 */
	public HibernateAnnotationIntrospector setUseTransient(boolean state) {
		_cfgCheckTransient = state;
		return this;
	}

	/*
    /**********************************************************************
    /* AnnotationIntrospector implementation/overrides
    /**********************************************************************
	 */

	@Override
	public boolean isHandled(Annotation a)
	{
		// We only care for one single type, for now:
		return (a.annotationType() == Transient.class);
	}

	public boolean isIgnorableConstructor(AnnotatedConstructor c)
	{
		return _cfgCheckTransient && c.hasAnnotation(Transient.class);
	}

	public boolean isIgnorableField(AnnotatedField f)
	{
		return _cfgCheckTransient && f.hasAnnotation(Transient.class);
	}

	public boolean isIgnorableMethod(AnnotatedMethod m)
	{
		return _cfgCheckTransient && m.hasAnnotation(Transient.class);
	}
}
