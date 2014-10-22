package org.emonocot.model.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang3.ObjectUtils;
import org.emonocot.api.job.TermFactory;
import org.gbif.dwc.terms.Term;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class TermUserType implements UserType {
	
	private static TermFactory TERM_FACTORY = new TermFactory();

	@Override
	public boolean isMutable() {
	    return false;
	}
	 
	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
	    return ObjectUtils.equals(x, y);
	}
	 
	@Override
	public int hashCode(Object x) throws HibernateException {
	    assert (x != null);
	    return x.hashCode();
	}
	 
	@Override
	public Object deepCopy(Object value) throws HibernateException {
	    return value;
	}
	 
	@Override
	public Object replace(Object original, Object target, Object owner)
	        throws HibernateException {
	    return original;
	}
	 
	@Override
	public Serializable disassemble(Object value) throws HibernateException {
	    return (Serializable) value;
	}
	 
	@Override
	public Object assemble(Serializable cached, Object owner)
	        throws HibernateException {
	    return cached;
	}

	@Override
	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner)
			throws HibernateException, SQLException {
		String value = (String) Hibernate.STRING.nullSafeGet(resultSet, names[0]);
        return ((value != null) ? TermUserType.TERM_FACTORY.findTerm(value) : null);
	}

	@Override
	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index)
			throws HibernateException, SQLException {
		 Term term = (Term)value;
		 Hibernate.STRING.nullSafeSet(preparedStatement,
	                (value != null) ? term.qualifiedName() : null, index);
	}

	@Override
	public Class returnedClass() {
		return Term.class;
	}

	@Override
	public int[] sqlTypes() {
		return new int[] { Types.VARCHAR };
	}

}
