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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.constants.MediaFormat;
import org.emonocot.model.constants.MediaType;

/**
 * @author jk00kg
 * 
 * See <a href="http://rs.gbif.org/extension/gbif/1.0/multimedia.xml">http://rs.gbif.org/extension/gbif/1.0/multimedia.xml</a>
 */
@MappedSuperclass
public class Multimedia extends SearchableObject implements NonOwned, Media {

    private static final long serialVersionUID = -8178055800655899536L;

    private String title;

    private String description;

    private MediaFormat format;

    private String creator;

    private String references;

    private String contributor;

    private String publisher;

    private String audience;

    private String source;

    private Set<Taxon> taxa = new HashSet<Taxon>();

    private MediaType type;

    /**
     * @return the title
     */
    @Size(max = 255)
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
     * @return the format
     */
    @Enumerated(EnumType.STRING)
    public MediaFormat getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(MediaFormat format) {
        this.format = format;
    }

    /**
     * @return the taxa
     */
    @Transient
    public Set<Taxon> getTaxa(){
        return taxa;
    };

    /**
     * @param taxa the taxa to set
     */
    public void setTaxa(Set<Taxon> taxa) {
        this.taxa = taxa;
    }

    /**
     * @return the creator
     */
    @Size(max = 255)
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
     * @return the references
     */
    @Column(name = "`references`")
    @Size(max = 255)
    public String getReferences() {
        return references;
    }

    /**
     * @param references the references to set
     */
    public void setReferences(String references) {
        this.references = references;
    }

    /**
     * @return the contributor
     */
    @Size(max = 255)
    public String getContributor() {
        return contributor;
    }

    /**
     * @param contributor the contributor to set
     */
    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    /**
     * @return the publisher
     */
    @Size(max = 255)
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the audience
     */
    @Size(max = 255)
    public String getAudience() {
        return audience;
    }

    /**
     * @param audience the audience to set
     */
    public void setAudience(String audience) {
        this.audience = audience;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    @Transient
    public MediaType getType() {
        return type;
    }

    /**
     * @param mediaType
     */
    public void setType(MediaType mediaType) {
        type = mediaType;
        
    }

    @Override
    public SolrInputDocument toSolrInputDocument() {
        SolrInputDocument sid = super.toSolrInputDocument();
        sid.addField("searchable.label_sort", getTitle());

        return sid;
    }
    
    
    @Transient
    public Long getId() {
        return null;
    }
    @Transient
    public Set<Annotation> getAnnotations() {
        return null;
    }
    public void setAnnotations(Set<Annotation> annotations) {}

}
