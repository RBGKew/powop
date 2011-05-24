package org.emonocot.model.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;

/**
 *
 * @author ben
 *
 */
public class GeographicRegionUserType implements UserType {

    /**
     *
     */
    private static final int[] SQL_TYPES = new int[] {Types.VARCHAR};

    /**
     * @return The class returned by nullSafeGet().
     */
    public final Class returnedClass() {
        return GeographicalRegion.class;
    }

    /**
     * @return Return the SQL type codes for the columns mapped by this type.
     */
    public final int[] sqlTypes() {
        return SQL_TYPES;
    }

    /**
     * Compare two instances of the class mapped by this type for persistence
     * "equality".
     *
     * @param x an instance.
     * @param y another instance.
     * @return True if the two instances are equal, false otherwise
     */
    public final boolean equals(final Object x, final Object y) {
         if (x == y) {
                return true;
         } else if (x == null || y == null) {
                return false;
         }
         GeographicalRegion regionX = (GeographicalRegion) x;
         GeographicalRegion regionY = (GeographicalRegion) y;

         return regionX.equals(regionY);
    }

    /**
     * Get a hashcode for the instance, consistent with persistence "equality".
     *
     * @param object an instance of the class mapped by this type
     * @return the hashcode of the object supplied
     */
    public final int hashCode(final Object object) {
        return object.hashCode();
    }

    /**
     * Retrieve an instance of the mapped class from a JDBC resultset.
     *
     * @param resultSet a JDBC result set
     * @param strings the column names
     * @param object the containing entity
     * @return the object, or null if it does not exist
     * @throws SQLException if the underlying SQL is incorrect
     */
    public final Object nullSafeGet(final ResultSet resultSet,
            final String[] strings, final Object object) throws SQLException {
        return nullSafeGet(resultSet, strings[0]);

    }

    /**
     * Retrieve an instance of the mapped class from a JDBC resultset.
     *
     * @param resultSet a JDBC result set
     * @param string the column name
     * @return the object, or null if it does not exist
     * @throws SQLException if the generated SQL is incorrect
     */
    public final Object nullSafeGet(final ResultSet resultSet,
            final String string) throws SQLException {
        String code = (String) StandardBasicTypes.STRING.nullSafeGet(
                resultSet, string);
        if (code.length() == 1) {
            return Continent.fromString(code);
        } else if (code.length() == 2) {
            return Region.fromString(code);
        } else if (code.length() == 3) {
            return Country.fromString(code);
        }
        throw new SQLException("Could not convert " + code
                + " into valid geographical region");
    }


    /**
     * Write an instance of the mapped class to a prepared statement.
     *
     * @param preparedStatement a JDBC prepared statement
     * @param value the object to write
     * @param index statement parameter index
     * @throws SQLException if the generated SQL is incorrect
     */
    public final void nullSafeSet(final PreparedStatement preparedStatement,
           final Object value, final int index) throws SQLException {
        if (value == null) {
            StandardBasicTypes.STRING
             .nullSafeSet(preparedStatement, null, index);
        } else {
            GeographicalRegion g = ((GeographicalRegion) value);
            StandardBasicTypes.STRING
              .nullSafeSet(preparedStatement, g.toString(), index);
        }
    }

    /**
     * Return a deep copy of the persistent state, stopping at entities and at
     * collections.
     *
     * @param value the object to be cloned, which may be null
     * @return a copy
     */
    public final Object deepCopy(final Object value) {
        if (value == null) {
            return null;
        }

        return ((GeographicalRegion) value);
    }

    /**
     * Are objects of this type mutable?
     *
     * @return true if the objects are mutable, false otherwise
     */
    public final boolean isMutable() {
        return false;
    }

    /**
     * Transform the object into its cacheable representation.
     *
     * @param value the object to be cached
     * @return a cachable representation of the object
     */
    public final Serializable disassemble(final Object value) {
        return (Serializable) value;
    }

    /**
     * Reconstruct an object from the cacheable representation.
     *
     * @param cached the object to be cached
     * @param owner the owner of the cached object
     * @return a reconstructed object from the cachable representation
     */
    public final Object assemble(final Serializable cached,
            final Object owner) {
        return cached;
    }

    /**
     * During merge, replace the existing (target) value in the entity we are
     * merging to with a new (original) value from the detached entity we are
     * merging.
     *
     * @param original the value from the detached entity being merged
     * @param target the value in the managed entity
     * @param owner the owner of the cached object
     * @return the value to be merged
     */
    public final Object replace(final Object original, final Object target,
            final Object owner) {
        return original;
    }
}
