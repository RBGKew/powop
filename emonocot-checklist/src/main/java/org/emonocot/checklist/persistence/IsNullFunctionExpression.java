package org.emonocot.checklist.persistence;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;
import org.hibernate.util.StringHelper;

/**
 *
 * @author ben
 *
 */
public class IsNullFunctionExpression implements Criterion {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private final String propertyName;

    /**
     *
     */
    private static final TypedValue[] NO_VALUES = new TypedValue[0];

    /**
     *
     * @param newPropertyName Set the property name
     */
    protected IsNullFunctionExpression(final String newPropertyName) {
            this.propertyName = newPropertyName;
    }

    /**
     * @param criteria Set the criteria
     * @param criteriaQuery Set the criteria query
     * @return the query as a string
     */
    public final String toSqlString(final Criteria criteria,
            final CriteriaQuery criteriaQuery) {
        String[] columns = criteriaQuery.findColumns(propertyName, criteria);
        String result = " and " + StringHelper.qualify("ISNULL(", columns)
                + " )";
        if (columns.length > 1) {
            result = '(' + result + ')';
        }
        return result;
    }

    /**
     * @param criteria Set the criteria
     * @param criteriaQuery Set the criteria query
     * @return the typed values
     */
    public final TypedValue[] getTypedValues(final Criteria criteria,
            final CriteriaQuery criteriaQuery) {
        return NO_VALUES;
    }

    /**
     * @return a string representation of the query
     */
    public final String toString() {
            return "ISNULL(" + propertyName + ")";
    }

}
