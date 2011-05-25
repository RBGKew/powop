package org.tdwg.voc;

import java.io.Serializable;
import java.net.URI;

import org.tdwg.DefinedTerm;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author ben
 *
 */
public class DefinedTermLinkType {
    /**
     *
     */
    private DefinedTerm tcomDefinedTerm;

    /**
     *
     */
    private GeographicRegion grGeographicRegion;

   /**
    *
    */
   @XStreamAlias("rdf:resource")
   @XStreamAsAttribute
   private URI resource;

   /**
    *
    * @return the resource
    */
   public final Serializable getResource() {
       return resource;
   }

   /**
    *
    * @param newResource Set the resource
    */
   public final void setResource(final URI newResource) {
       this.resource = newResource;
   }

   /**
    *
    */
    protected DefinedTermLinkType() {
    }

    /**
     *
     * @param newDefinedTerm Set the defined term in this link
     * @param useRelation use a relation or embed the the object
     */
    protected DefinedTermLinkType(final DefinedTerm newDefinedTerm,
            final boolean useRelation) {
        if (useRelation) {
            if (newDefinedTerm != null
                    && newDefinedTerm.getIdentifier() != null) {
                this.setResource(newDefinedTerm.getIdentifier());
            } else {
                if (newDefinedTerm instanceof GeographicRegion) {
                    this.grGeographicRegion = (GeographicRegion) tcomDefinedTerm;
                } else {
                    this.tcomDefinedTerm = newDefinedTerm;
                }
            }
        } else {
            if (newDefinedTerm instanceof GeographicRegion) {
                this.grGeographicRegion = (GeographicRegion) tcomDefinedTerm;
            } else {
                this.tcomDefinedTerm = newDefinedTerm;
            }
        }
    }
    /**
     *
     * @return the defined term in this link
     */
    public final DefinedTerm getDefinedTerm() {
        if (grGeographicRegion != null) {
            return grGeographicRegion;
        } else {
            return tcomDefinedTerm;
        }
    }

    /**
     *
     * @param newDefinedTerm Set the defined term in this link
     */
    protected final void setDefinedTerm(final DefinedTerm newDefinedTerm) {
        if (newDefinedTerm instanceof GeographicRegion) {
            this.grGeographicRegion = (GeographicRegion) tcomDefinedTerm;
        } else {
            this.tcomDefinedTerm = newDefinedTerm;
        }
    }
}
