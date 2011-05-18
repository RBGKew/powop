package org.tdwg.voc;

import java.io.Serializable;
import java.net.URI;

import org.emonocot.model.marshall.UriConverter;
import org.tdwg.DefinedTerm;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 *
 * @author ben
 *
 */
public class DefinedTermLinkType {
    /**
     *
     */
    private DefinedTerm definedTerm;

    /**
     *
     */
    private GeographicRegion geographicRegion;

   /**
    *
    */
   @XStreamAlias("rdf:resource")
   @XStreamAsAttribute
   @XStreamConverter(UriConverter.class)
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
                    this.geographicRegion = (GeographicRegion) definedTerm;
                } else {
                    this.definedTerm = newDefinedTerm;
                }
            }
        } else {
            if (newDefinedTerm instanceof GeographicRegion) {
                this.geographicRegion = (GeographicRegion) definedTerm;
            } else {
                this.definedTerm = newDefinedTerm;
            }
        }
    }
    /**
     *
     * @return the defined term in this link
     */
    protected final DefinedTerm getDefinedTerm() {
        return definedTerm;
    }

    /**
     *
     * @param newDefinedTerm Set the defined term in this link
     */
    protected final void setDefinedTerm(final DefinedTerm newDefinedTerm) {
        if (newDefinedTerm instanceof GeographicRegion) {
            this.geographicRegion = (GeographicRegion) definedTerm;
        } else {
            this.definedTerm = newDefinedTerm;
        }
    }
}
