package org.emonocot.model.source;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.hibernate.IdentifierBridge;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.ClassBridges;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;

/**
 * Class that represents the authority an object is harvested from.
 *
 * @author ben
 *
 */
@Entity
@Indexed(index = "org.emonocot.model.common.SearchableObject")
@ClassBridges({
    @ClassBridge(name = "name",
            impl = IdentifierBridge.class),
    @ClassBridge(name = "label",
                    impl = IdentifierBridge.class,
                    index = Index.UN_TOKENIZED)
})
public class Source extends SearchableObject {

    /**
     *
     */
    private static final long serialVersionUID = -2463044801110563816L;

   /**
    *
    */
   private String uri;

   /**
    *
    */
   private Long id;

   /**
   *
   * @param newId
   *            Set the identifier of this object.
   */
  public void setId(Long newId) {
      this.id = newId;
  }

  /**
   *
   * @return Get the identifier for this object.
   */
  @Id
  @GeneratedValue(generator = "system-increment")
  @DocumentId
  public Long getId() {
      return id;
  }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
    *
    * @return the class name
    */
   @Transient
   @JsonIgnore
   public final String getClassName() {
       return "Source";
   }
}
