package org.emonocot.model.key;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed(index = "org.emonocot.model.common.SearchableObject")
//TODO Consider @ClassBridge(impl = org.emonocot.model.hibernate.TaxonomyBridge.class)
public class IdentificationKey extends SearchableObject{

    /**
     *
     */
    private static final long serialVersionUID = 7893868318442314512L;

   /**
    *
    */
    private Long id;

    /**
     *
     */
    private String title;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private Taxon taxon;

    /**
     *
     */
    @Id
    @GeneratedValue(generator = "system-increment")
    @DocumentId
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    @Fields({ @Field, @Field(name = "label", index = Index.UN_TOKENIZED) })
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    @Lob
    @Field
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the taxon covered by the key
     * e.g. A key to grass genera should return Poaceae
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @IndexedEmbedded(depth = 1)
    public Taxon getTaxon() {
        return taxon;
    }

    /**
     * @param taxon the taxon to set
     */
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }
}
