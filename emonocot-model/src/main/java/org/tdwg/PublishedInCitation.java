package org.tdwg;

import org.tdwg.voc.LinkType;
import org.tdwg.voc.PublicationCitation;

/**
 *
 * @author ben
 *
 */
public class PublishedInCitation extends LinkType {
   /**
    *
    */
   private PublicationCitation tpubPublicationCitation;

   /**
    *
    */
   protected PublishedInCitation() {
   }

   /**
    *
    * @param newPublicationCitation Set the publication citation
    * @param useRelation Set as a relation, not as an object
    */
   public PublishedInCitation(
           final PublicationCitation newPublicationCitation,
           final boolean useRelation) {
       if (useRelation) {
           if (newPublicationCitation != null
                   && newPublicationCitation.getIdentifier() != null) {
               this.setResource(newPublicationCitation.getIdentifier());
           } else {
               this.tpubPublicationCitation = newPublicationCitation;
           }
       } else {
           this.tpubPublicationCitation = newPublicationCitation;
       }
   }

   /**
    *
    * @param newPublicationCitation Set the publication citation
    */
   protected final void setPublicationCitation(
           final PublicationCitation newPublicationCitation) {
       this.tpubPublicationCitation = newPublicationCitation;
   }

   /**
    *
    * @return the publication citation
    */
   public final PublicationCitation getPublicationCitation() {
       return tpubPublicationCitation;
   }

}
