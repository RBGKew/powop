package org.emonocot.model.description;

import javax.persistence.Entity;

import org.apache.commons.lang.ObjectUtils;
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
    private String content;

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
        return super.hashCode() + this.content.hashCode();
    }
}
