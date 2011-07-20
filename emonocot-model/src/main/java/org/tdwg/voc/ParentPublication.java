package org.tdwg.voc;

/**
*
* @author ben
*
*/
public class ParentPublication extends LinkType {

   /**
    *
    */
   private PublicationCitation tpubPublicationCitation;

   /**
    *
    */
   protected ParentPublication() {
   }

   /**
    *
    * @param publicationCitation set the publication citation of this element
    */
   protected ParentPublication(final PublicationCitation publicationCitation) {
       this.tpubPublicationCitation = publicationCitation;
   }

   /**
    *
    * @return the publicationCitation of this element
    */
   protected final PublicationCitation getPublicationCitation() {
       return tpubPublicationCitation;
   }

   /**
    *
    * @param publicationCitation set the publication citation of this element
    */
   protected final void setRelationship(
           final PublicationCitation publicationCitation) {
       this.tpubPublicationCitation = publicationCitation;
   }
}
