package org.tdwg.ubif;

import java.util.ArrayList;
import java.util.List;

import org.tdwg.ubif.marshall.xml.Ignore;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author ben
 *
 */
public class Dataset {

    /**
     *
     */
    @XStreamAsAttribute
    private String id;

    /**
     *
     */
   @XStreamAsAttribute
   private String debuglabel;

   /**
    * We ignore these elements as we don't need to marshall them.
    */
   @XStreamAlias("RevisionData")
   private RevisionData revisionData;

   /**
    * We ignore these elements as we don't need to marshall them.
    */
   @XStreamAlias("TaxonNames")
   private Ignore taxonNames;

   /**
    * We ignore these elements as we don't need to marshall them.
    */
   @XStreamAlias("TaxonHierarchies")
   private Ignore taxonHierarchies;

   /**
    * We ignore these elements as we don't need to marshall them.
    */
   @XStreamAlias("DescriptiveConcepts")
   private Ignore descriptiveConcepts;

   /**
    * We ignore these elements as we don't need to marshall them.
    */
   @XStreamAlias("Characters")
   private Ignore characters;

   /**
    * We ignore these elements as we don't need to marshall them.
    */
   @XStreamAlias("CharacterTrees")
   private Ignore characterTrees;

   /**
    * We ignore these elements as we don't need to marshall them.
    */
   @XStreamAlias("CodedDescriptions")
   private Ignore codedDescriptions;

   /**
    *
    */
   @XStreamAlias("Agents")
   private List<Agent> agents = new ArrayList<Agent>();

   /**
    * We ignore these elements as we don't need to marshall them.
    */
   @XStreamAlias("MediaObjects")
   private Ignore mediaObjects;

    /**
     *
     */
    @XStreamAlias("Representation")
    private Representation representation;

    /**
     * @return the representation
     */
    public final Representation getRepresentation() {
        return representation;
    }

    /**
     * @param newRepresentation the representation to set
     */
    public final void setRepresentation(
            final Representation newRepresentation) {
        this.representation = newRepresentation;
    }

    /**
     *
     * @param newId Set the id
     */
    public final void setId(final String newId) {
        this.id = newId;
    }

    /**
     *
     * @return the id
     */
    public final String getId() {
        return id;
    }

    /**
     * @return the debuglabel
     */
    public final String getDebuglabel() {
        return debuglabel;
    }

    /**
     * @param newDebugLabel the debuglabel to set
     */
    public final void setDebuglabel(final String newDebugLabel) {
        this.debuglabel = newDebugLabel;
    }

    /**
     * @return the revisionData
     */
    public final RevisionData getRevisionData() {
        return revisionData;
    }

    /**
     * @param newRevisionData the revisionData to set
     */
    public final void setRevisionData(final RevisionData newRevisionData) {
        this.revisionData = newRevisionData;
    }

    /**
     * @return the agents
     */
    public final List<Agent> getAgents() {
        return agents;
    }

    /**
     * @param newAgents the agents to set
     */
    public final void setAgents(final List<Agent> newAgents) {
        this.agents = newAgents;
    }
}
