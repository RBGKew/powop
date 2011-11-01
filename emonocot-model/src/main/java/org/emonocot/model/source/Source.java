package org.emonocot.model.source;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.emonocot.model.common.BaseData;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;

/**
 * Class that represents the authority an object is harvested from.
 *
 * @author ben
 *
 */
@Entity
@Indexed
public class Source extends BaseData {

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
