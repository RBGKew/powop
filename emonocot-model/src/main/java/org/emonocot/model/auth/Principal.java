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
package org.emonocot.model.auth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.emonocot.model.Base;
import org.emonocot.model.Searchable;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
@Entity
public abstract class Principal extends Base implements Searchable {

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
     * @return The unique identifier of the object
     */
    @NaturalId
    @NotEmpty
    public String getIdentifier() {
        return identifier;
    }

    /**
     *
     * @param identifier
     *            Set the unique identifier of the object
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

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
    @GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
    public Long getId() {
        return id;
    }

    /**
     *
     * @return the date this principal was created
     */
    @Type(type = "dateTimeUserType")
    public DateTime getCreated() {
        return created;
    }

    /**
     *
     * @param created
     *            Set the date this principal was created
     */
    public void setCreated(DateTime created) {
        this.created = created;
    }

    /**
     *
     * @return the date this princpal was modified
     */
    @Type(type = "dateTimeUserType")
    public DateTime getModified() {
        return modified;
    }

    /**
     *
     * @param modified
     *            Set the date this principal was modified
     */
    public void setModified(DateTime modified) {
        this.modified = modified;
    }
}
