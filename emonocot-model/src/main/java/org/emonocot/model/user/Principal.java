package org.emonocot.model.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.ObjectUtils;
import org.emonocot.model.hibernate.DateTimeBridge;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Parameter;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
@Entity
public abstract class Principal {

   /**
    *
    */
    private DateTime created;

   /**
    *
    */
    private DateTime modified;

   /**
    *
    */
    private Long id;

   /**
    *
    */
    private String identifier;

    /**
     *
     */
    private static final long serialVersionUID = -2461535344191283847L;

    /**
     *
     * @return the date this principal was created
     */
    @Type(type = "dateTimeUserType")
    @FieldBridge(impl = DateTimeBridge.class, params = { @Parameter(name = "resolution", value = "MILLISECOND") })
    public DateTime getCreated() {
        return created;
    }

    /**
     *
     * @param created Set the date this principal was created
     */
    public void setCreated(DateTime created) {
        this.created = created;
    }

    /**
     *
     * @return the date this princpal was modified
     */
    @Type(type = "dateTimeUserType")
    @FieldBridge(impl = DateTimeBridge.class, params = { @Parameter(name = "resolution", value = "MILLISECOND") })
    public DateTime getModified() {
        return modified;
    }

    /**
     *
     * @param modified Set the date this principal was modified
     */
    public void setModified(DateTime modified) {
        this.modified = modified;
    }

    /**
     *
     * @return the id
     */
    @Id
    @GeneratedValue(generator = "system-increment")
    @DocumentId
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id Set the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return the identifier of this principal
     */
    @NaturalId
    public String getIdentifier() {
        return identifier;
    }

    /**
     *
     * @param identifier Set the identifier
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public final boolean equals(final Object other) {
        // check for self-comparison
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        Principal principal = (Principal) other;
        return ObjectUtils.equals(this.identifier, principal.identifier);
    }

    @Override
    public final int hashCode() {
        return ObjectUtils.hashCode(this.identifier);
    }
}
