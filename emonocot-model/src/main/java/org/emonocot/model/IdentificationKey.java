/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.apache.solr.common.SolrInputDocument;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.emonocot.model.constants.MediaType;
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
public class IdentificationKey extends Multimedia {

    private static final long serialVersionUID = 7893868318442314512L;

    private Long id;
    
    private Set<Taxon> taxa = new HashSet<Taxon>();
    
    private Set<Annotation> annotations = new HashSet<Annotation>();
    
    private String matrix;
    
    private List<Comment> comments = new ArrayList<Comment>();

    @Id
    @GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Taxon_IdentificationKey", joinColumns = {@JoinColumn(name = "keys_id")}, inverseJoinColumns = {@JoinColumn(name = "Taxon_id")})
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE })
    @JsonSerialize(contentUsing = TaxonSerializer.class)
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
    
    /**
	 * @return the comments
	 */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "commentPage_id")
    @OrderBy("created DESC")
    @Where(clause = "commentPage_type = 'IdentificationKey'")
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
    @JsonIgnore
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
    @JsonIgnore
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
    
    @Override
    @Transient
    public MediaType getType() {
        return MediaType.Dataset;
    }

	@Override
    public SolrInputDocument toSolrInputDocument() {
    	SolrInputDocument sid = super.toSolrInputDocument();
    	StringBuilder summary = new StringBuilder().append(getTitle()).append(" ")
    	.append(getCreator()).append(" ").append(getDescription());
    	if(getTaxa() != null) {
    		boolean first = true; 
    		for(Taxon t : getTaxa()) {
    			if(first) {
        	        addField(sid,"taxon.order_s", t.getOrder());
        	        addField(sid,"taxon.subgenus_s", t.getSubgenus());
    			}
    	        addField(sid,"taxon.family_ss", t.getFamily());
    	        addField(sid,"taxon.genus_ss", t.getGenus());    	    
    	        addField(sid,"taxon.subfamily_ss", t.getSubfamily());    	    
    	        addField(sid,"taxon.subtribe_ss", t.getSubtribe());
    	        addField(sid,"taxon.tribe_ss", t.getTribe());
    	        summary.append(" ").append(t.getClazz())
    	        .append(" ").append(t.getFamily())
    	        .append(" ").append(t.getGenus())
          	    .append(" ").append(t.getKingdom())
    	        .append(" ").append(t.getOrder())
    	        .append(" ").append(t.getPhylum())
    	        .append(" ").append(t.getSubfamily())
    	        .append(" ").append(t.getSubgenus())
    	        .append(" ").append(t.getSubtribe())
    	        .append(" ").append(t.getTribe());
    	        first = false;
    		}
    	}
    	sid.addField("searchable.solrsummary_t", summary.toString());
    	
    	return sid;
    }
}
