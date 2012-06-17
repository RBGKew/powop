package org.emonocot.api.job;

import java.io.Serializable;

/**
 *
 * @author ben
 *
 */
public class JobInstanceInfo implements Serializable {

    /**
     *
     */
    private String resource;

    /**
     * @return the resource
     */
    public final String getResource() {
        return resource;
    }

    /**
     * @param newResource the resource to set
     */
    public final void setResource(final String newResource) {
        this.resource = newResource;
    }

}
