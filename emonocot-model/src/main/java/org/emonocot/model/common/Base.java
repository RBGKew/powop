package org.emonocot.model.common;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.annotations.NaturalId;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;

/**
 *
 * @author ben
 *
 */
@MappedSuperclass
public abstract class Base implements Serializable {

    /**
     *
     */
    private String identifier;

    /**
     *
     * @return The unique identifier of the object
     */
    @Field
    @NaturalId
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object other) {
        // check for self-comparison
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        Base base = (Base) other;
        return ObjectUtils.equals(this.identifier, base.identifier);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(this.identifier);
    }

    /**
     *
     * @param identifier
     *            Set the unique identifier of the object
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
