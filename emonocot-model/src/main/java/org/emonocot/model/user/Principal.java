package org.emonocot.model.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.ObjectUtils;
import org.emonocot.model.common.Base;
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
public abstract class Principal extends Base {

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
    private static final long serialVersionUID = -2461535344191283847L;

    /**
    *
    * @param newId
    *            Set the identifier of this object.
    */
   public void setId(Long newId) {
       this.id = newId;
   }

   /**
    *
    * @return Get the identifier for this object.
    */
   @Id
   @GeneratedValue(generator = "system-increment")
   @DocumentId
   public Long getId() {
       return id;
   }

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
}
