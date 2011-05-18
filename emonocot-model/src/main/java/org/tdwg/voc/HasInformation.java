package org.tdwg.voc;

import java.io.Serializable;
import java.net.URI;

import org.emonocot.model.marshall.UriConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 *
 * @author ben
 *
 */
public class HasInformation {
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
    private InfoItem spmInfoItem;

    /**
     *
     */
    private Distribution spmiDistribution;

    /**
     *
     */
    protected HasInformation() {
    }

    /**
     *
     * @param newInfoItem the information
     */
    protected HasInformation(final InfoItem newInfoItem) {
        if (newInfoItem instanceof Distribution) {
            this.spmiDistribution = (Distribution) newInfoItem;
        } else {
            this.spmInfoItem = newInfoItem;
        }
    }

    /**
     *
     * @return the information
     */
    protected final InfoItem getInfoItem() {
        if (this.spmiDistribution == null) {
            return spmInfoItem;
        } else {
            return spmiDistribution;
        }
    }

    /**
     *
     * @param newInfoItem Set the information
     */
    protected final void setInfoItem(final InfoItem newInfoItem) {
        if (newInfoItem instanceof Distribution) {
            this.spmiDistribution = (Distribution) newInfoItem;
        } else {
            this.spmInfoItem = newInfoItem;
        }
    }
}
