package org.emonocot.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;

/**
 *
 * @author ben
 *
 */
@Entity
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
    private String creator;
    
    private Set<Annotation> annotations = new HashSet<Annotation>();

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
     *
     */
    private String matrix;

    /**
     *
     */
    @Id
    @GeneratedValue(generator = "system-increment")
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
    
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'IdentificationKey'")
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
    @JsonIgnore
    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * @param annotations
     *            the annotations to set
     */
    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }
    
    @Override
    public SolrInputDocument toSolrInputDocument() {
    	SolrInputDocument sid = super.toSolrInputDocument();
    	sid.addField("searchable.label_sort", getTitle());
    	sid.addField("key.title_t", getTitle());
    	addField(sid,"key.creator_t", getCreator());
    	addField(sid,"key.description_t", getDescription());
    	StringBuilder summary = new StringBuilder().append(getTitle()).append(" ")
    	.append(getCreator()).append(" ").append(getDescription());
    	if(getTaxon() != null) {    		
    		addField(sid,"taxon.class_s", getTaxon().getClazz());
    	    addField(sid,"taxon.family_s", getTaxon().getFamily());
    	    addField(sid,"taxon.genus_s", getTaxon().getGenus());
    	    addField(sid,"taxon.kingdom_s", getTaxon().getKingdom());
    	    addField(sid,"taxon.order_s", getTaxon().getOrder());
    	    addField(sid,"taxon.phylum_s", getTaxon().getPhylum());
    	    addField(sid,"taxon.subfamily_s", getTaxon().getSubfamily());
    	    addField(sid,"taxon.subgenus_s", getTaxon().getSubgenus());
    	    addField(sid,"taxon.subtribe_s", getTaxon().getSubtribe());
    	    addField(sid,"taxon.tribe_s", getTaxon().getTribe());
    	    summary.append(" ").append(getTaxon().getClazz())
    	    .append(" ").append(getTaxon().getClazz())
    	    .append(" ").append(getTaxon().getFamily())
    	    .append(" ").append(getTaxon().getGenus())
    	    .append(" ").append(getTaxon().getKingdom())
    	    .append(" ").append(getTaxon().getOrder())
    	    .append(" ").append(getTaxon().getPhylum())
    	    .append(" ").append(getTaxon().getSubfamily())
    	    .append(" ").append(getTaxon().getSubgenus())
    	    .append(" ").append(getTaxon().getSubtribe())
    	    .append(" ").append(getTaxon().getTribe());
    	}
    	sid.addField("searchable.solrsummary_t", summary.toString());
    	
    	return sid;
    }
}
