package org.emonocot.checklist.test;

import java.io.Serializable;

import org.emonocot.checklist.model.IdentifiableEntity;
import org.joda.time.DateTime;
/**
 *
 * @author ben
 *
 */
public class DummyIdentifiableEntity implements IdentifiableEntity {
    /**
     *
     */
    private Serializable identifier;

   /**
    *
    */
   private String title;

  /**
   *
   */
   private DateTime created;

    /**
     * @return the identifier
     */
    public final Serializable getIdentifier() {
        return identifier;
    }

    /**
     * @param newIdentifier Set the new identifier
     */
    public final void setIdentifier(final Serializable newIdentifier) {
        this.identifier = newIdentifier;
    }

    /**
     *
     * @param string Set the title of the entity
     */
    public final void setTitle(final String string) {
        this.title = string;
    }

    /**
     *
     * @return the title of this object
     */
    public final String getTitle() {
        return this.title;
    }

    /**
     *
     * @return the date this object was created
     */
    public final DateTime getCreated() {
        return created;
    }

    /**
     *
     * @param newCreated Set the created date
     */
    public final void setCreated(final DateTime newCreated) {
        this.created = newCreated;
    }
}
