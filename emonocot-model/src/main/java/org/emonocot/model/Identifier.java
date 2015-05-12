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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;

/**
 *
 * @author ben
 *
 */
@Entity
public class Identifier extends OwnedEntity {

    private static final long serialVersionUID = 8866662399880411558L;

    private String title;

    private String subject;

    private String format;

    private Taxon taxon;

    private Set<Annotation> annotations = new HashSet<Annotation>();

    private Long id;

   /**
    *
    * @return the format of the image
    */
    @Size(max = 255)
    public String getFormat() {
       return format;
    }

   /**
    *
    * @param format Set the format of the image
    */
   public void setFormat(String format) {
       this.format = format;
   }

   /**
    *
    * @return the subject of this identifier
    */
   @Size(max = 255)
   public String getSubject() {
       return subject;
   }

   /**
    *
    * @param subject Set the subject of this identifier
    */
   public void setSubject(String subject) {
       this.subject = subject;
   }

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
    @GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
    public Long getId() {
        return id;
    }

    /**
     *
     * @return the title
     */
    @Size(max = 255)
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param newTitle
     *            Set the title
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     * The taxon (page) this identifier is related to.
     *
     * @return the taxon this image is of
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("identifier-taxon")
    public Taxon getTaxon() {
        return taxon;
    }

    /**
     *
     * @param taxon
     *            Set the taxon this image is of
     */
    @JsonBackReference("identifier-taxon")
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    /**
     *
     * @return the name of this class
     */
    @Transient
    @JsonIgnore
    public String getClassName() {
        return "Identifier";
    }

    /**
     * @return the annotations
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'Identifier'")
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
    public String toString() {
    	StringBuffer stringBuffer = new StringBuffer();
    	stringBuffer.append(identifier);
    	if(title != null) {
    		stringBuffer.append(": \"" + title + "\"");
    	}
    	return stringBuffer.toString();
    }
}
