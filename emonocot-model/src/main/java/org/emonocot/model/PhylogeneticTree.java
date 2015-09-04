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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.constants.MediaType;
import org.emonocot.model.marshall.json.ReferenceDeserializer;
import org.emonocot.model.marshall.json.ReferenceSerializer;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
public class PhylogeneticTree extends Multimedia {

	/**
	 *
	 */
	private static final long serialVersionUID = 6377124432983928528L;

	private Long id;

	private Set<Taxon> taxa = new HashSet<Taxon>();

	private Set<Taxon> leaves = new HashSet<Taxon>();

	private Set<Annotation> annotations = new HashSet<Annotation>();

	private String phylogeny;

	private List<Comment> comments = new ArrayList<Comment>();

	private Long numberOfExternalNodes;

	private Reference bibliographicReference;

	private boolean hasBranchLengths;

	public Long getNumberOfExternalNodes() {
		return numberOfExternalNodes;
	}

	public void setNumberOfExternalNodes(Long numberOfExternalNodes) {
		this.numberOfExternalNodes = numberOfExternalNodes;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	@JsonSerialize(using = ReferenceSerializer.class)
	public Reference getBibliographicReference() {
		return bibliographicReference;
	}

	@JsonDeserialize(using = ReferenceDeserializer.class)
	public void setBibliographicReference(Reference bibliographicReference) {
		this.bibliographicReference = bibliographicReference;
	}

	@Override
	@Id
	@GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
	public Long getId() {
		return id;
	}

	@Override
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "annotatedObjId")
	@Where(clause = "annotatedObjType = 'PhylogeneticTree'")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
	@JsonIgnore
	public Set<Annotation> getAnnotations() {
		return annotations;
	}

	@Override
	public void setAnnotations(Set<Annotation> annotations) {
		this.annotations = annotations;
	}

	@Override
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Taxon_PhylogeneticTree", joinColumns = {@JoinColumn(name = "trees_id")}, inverseJoinColumns = {@JoinColumn(name = "Taxon_id")})
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE })
	@JsonSerialize(contentUsing = TaxonSerializer.class)
	public Set<Taxon> getTaxa() {
		return taxa;
	}

	@Override
	@JsonDeserialize(contentUsing = TaxonDeserializer.class)
	public void setTaxa(Set<Taxon> taxa) {
		this.taxa = taxa;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "PhylogeneticTree_Taxon", joinColumns = {@JoinColumn(name = "PhylogeneticTree_id")}, inverseJoinColumns = {@JoinColumn(name = "leaves_id")})
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE })
	@JsonSerialize(contentUsing = TaxonSerializer.class)
	public Set<Taxon> getLeaves() {
		return leaves;
	}

	@JsonDeserialize(contentUsing = TaxonDeserializer.class)
	public void setLeaves(Set<Taxon> leaves) {
		this.leaves = leaves;
	}

	@Lob
	public String getPhylogeny() {
		return phylogeny;
	}

	public void setPhylogeny(String phylogeny) {
		this.phylogeny = phylogeny;
	}

	public void setHasBranchLengths(boolean hasBranchLengths) {
		this.hasBranchLengths = hasBranchLengths;
	}

	public boolean getHasBranchLengths() {
		return hasBranchLengths;
	}

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "commentPage_id")
	@OrderBy("created DESC")
	@Where(clause = "commentPage_type = 'PhylogeneticTree'")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
	@JsonIgnore
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @param comments - Comments made about this tree
	 */
	@JsonIgnore
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.emonocot.model.Multimedia#getType()
	 */
	@Override
	@Transient
	public MediaType getType() {
		return MediaType.InteractiveResource;
	}

	@Override
	public SolrInputDocument toSolrInputDocument() {
		SolrInputDocument sid = super.toSolrInputDocument();
		//sid.addField("phylogeny.title_t", getTitle());
		//addField(sid,"phylogeny.creator_t", getCreator());
		//addField(sid,"phylogeny.description_t", getDescription());
		StringBuilder summary = new StringBuilder().append(getTitle()).append(" ")
				.append(getCreator()).append(" ").append(getDescription());
		if(getTaxa() != null) {
			boolean first = true;
			for(Taxon t : getTaxa()) {
				if(first) {
					//addField(sid,"taxon.class_s", t.getClazz());
					//addField(sid,"taxon.kingdom_s", t.getKingdom());
					//addField(sid,"taxon.phylum_s", t.getPhylum());
					addField(sid,"taxon.subgenus_s", t.getSubgenus());
					addField(sid,"taxon.order_s", t.getOrder());
				}
				addField(sid,"taxon.family_ss", t.getFamily());
				addField(sid,"taxon.genus_ss", t.getGenus());
				addField(sid,"taxon.subfamily_ss", t.getSubfamily());
				addField(sid,"taxon.subtribe_ss", t.getSubtribe());
				addField(sid,"taxon.tribe_ss", t.getTribe());
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
				first = false;
			}
		}
		sid.addField("searchable.solrsummary_t", summary.toString());

		return sid;
	}
}
