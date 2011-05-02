package org.emonocot.job.scratchpads.model;

/**
 *
 * @author ben
 *
 */
public class EoLAgent {

    /**
     * This maps to the attribute role.
     */
    private String role;

    /**
     * This maps to the node value.
     */
    private String uri;

    /**
     *
     * @return The role of this agent.
     */
    public final String getRole() {
        return role;
    }

    /**
     *
     * @return The uri of this agent.
     */
    public final String getURI() {
        return uri;
    }

    /**
     *
     * @param newUri Set the uri of this agent.
     */
    public final void setURI(final String newUri) {
        this.uri = newUri;
    }

    /**
     *
     * @param newRole Set the role of this agent.
     */
    public final void setRole(final String newRole) {
        this.role = newRole;
    }
}
