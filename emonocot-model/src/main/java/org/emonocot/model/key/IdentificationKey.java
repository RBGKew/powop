package org.emonocot.model.key;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.hibernate.TaxonomyBridge;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ClassBridge;
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
@ClassBridge(name = "taxon", impl = TaxonomyBridge.class, index = Index.UN_TOKENIZED,
        analyzer = @Analyzer(definition = "facetAnalyzer"))
public class IdentificationKey extends SearchableObject {

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
    private String matrix;

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
    @JsonSerialize(using = TaxonSerializer.class)
    public Taxon getTaxon() {
        return taxon;
    }

    /**
     * @param taxon the taxon to set
     */
    @JsonDeserialize(using = TaxonDeserializer.class)
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    /**
     *
     * @param matrix Set the matrix
     */
    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }

    /**
     *
     * @return the matrix
     */
    @Lob
    public String getMatrix() {
        return matrix;
    }
}
