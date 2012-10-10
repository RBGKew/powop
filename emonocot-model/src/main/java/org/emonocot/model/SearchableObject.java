package org.emonocot.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author ben
 *
 */
@MappedSuperclass
public abstract class SearchableObject extends BaseData {

    /**
     *
     */
    private static final long serialVersionUID = 4960825789689641206L;

    /**
     *
     * @return the simple name of the implementing class
     */
    @Transient
    @JsonIgnore
    public String getClassName() {
        return getClass().getSimpleName();
    }

}
