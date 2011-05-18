package org.tdwg.voc;

/**
*
* @author ben
*
*/
public class HasRelationship extends LinkType {

   /**
    *
    */
   private Relationship tcRelationship;

   /**
    *
    */
   protected HasRelationship() {
   }

   /**
    *
    * @param relationship Set the relationship of this element
    */
   protected HasRelationship(final Relationship relationship) {
       this.tcRelationship = relationship;
   }

   /**
    *
    * @return the relationship of this element
    */
   protected final Relationship getRelationship() {
       return tcRelationship;
   }

   /**
    *
    * @param relationship Set the relationship of this element
    */
   protected final void setRelationship(final Relationship relationship) {
       this.tcRelationship = relationship;
   }
}
