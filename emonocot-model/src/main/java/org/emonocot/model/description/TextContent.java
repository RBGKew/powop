package org.emonocot.model.description;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.ObjectUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.emonocot.model.common.Annotation;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed
public class TextContent extends Content {

    /**
     *
     */
    private static final long serialVersionUID = -177666938449346483L;
    /**
     *
     */
    private String content;

   /**
    *
    */
   private Set<Annotation> annotations = new HashSet<Annotation>();

    /**
     *
     * @param newContent Set the content of this object.
     */
    public void setContent(String newContent) {
        this.content = newContent;
    }

    /**
     *
     * @return the content as a string
     */
    @Lob
    @Field
    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        TextContent otherContent = (TextContent) other;
        return ObjectUtils.equals(this.content, otherContent.content);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + (this.content == null ? 0 : this.content.hashCode());
    }

    @Transient
    @JsonIgnore
    public final String getClassName() {
      return "TextContent";
    }

    /**
     * @return the annotations
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'TextContent'")
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
}
