package org.emonocot.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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

    private static final long serialVersionUID = 7893868318442314512L;

    private Long id;

    private String title;

    private String description;

    private Taxon taxon;
    
    private Set<Taxon> taxa = new HashSet<Taxon>();
    
    private String creator;
    
    private Set<Annotation> annotations = new HashSet<Annotation>();
    
    private String matrix;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

    @Id
    @GeneratedValue(generator = "system-increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Lob
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Taxon_IdentificationKey", joinColumns = {@JoinColumn(name = "keys_id")}, inverseJoinColumns = {@JoinColumn(name = "Taxon_id")})
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE })
    public Set<Taxon> getTaxa() {
        return taxa;
    }

    /**
     * @param taxon the taxon to set
     */
    @JsonDeserialize(contentUsing = TaxonDeserializer.class)
    public void setTaxa(Set<Taxon> taxa) {
        this.taxa = taxa;
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
    	if(getTaxa() != null) {
    		for(Taxon t : getTaxa()) {
    		addField(sid,"taxon.class_s", t.getClazz());
    	    addField(sid,"taxon.family_s", t.getFamily());
    	    addField(sid,"taxon.genus_s", t.getGenus());
    	    addField(sid,"taxon.kingdom_s", t.getKingdom());
    	    addField(sid,"taxon.order_s", t.getOrder());
    	    addField(sid,"taxon.phylum_s", t.getPhylum());
    	    addField(sid,"taxon.subfamily_s", t.getSubfamily());
    	    addField(sid,"taxon.subgenus_s", t.getSubgenus());
    	    addField(sid,"taxon.subtribe_s", t.getSubtribe());
    	    addField(sid,"taxon.tribe_s", t.getTribe());
    	    summary.append(" ").append(t.getClazz())
    	    .append(" ").append(t.getClazz())
    	    .append(" ").append(t.getFamily())
    	    .append(" ").append(t.getGenus())
    	    .append(" ").append(t.getKingdom())
    	    .append(" ").append(t.getOrder())
    	    .append(" ").append(t.getPhylum())
    	    .append(" ").append(t.getSubfamily())
    	    .append(" ").append(t.getSubgenus())
    	    .append(" ").append(t.getSubtribe())
    	    .append(" ").append(t.getTribe());
    		}
    	}
    	sid.addField("searchable.solrsummary_t", summary.toString());
    	
    	return sid;
    }
}
