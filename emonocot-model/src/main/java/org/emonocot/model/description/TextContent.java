package org.emonocot.model.description;

import org.apache.commons.lang.ObjectUtils;

/**
 *
 * @author ben
 *
 */
public class TextContent extends Content {
    /**
     *
     */
    private String content;

    /**
     *
     * @param newContent Set the content of this object.
     */
    public final void setContent(final String newContent) {
        this.content = newContent;
    }

    /**
     *
     * @return the content as a string
     */
    public final String getContent() {
        return content;
    }

    @Override
    public final boolean equals(final Object other) {
        if (!super.equals(other)) {
            return false;
        }
        TextContent otherContent = (TextContent) other;
        return ObjectUtils.equals(this.content, otherContent.content);
    }

    @Override
    public final int hashCode() {
        return super.hashCode() + this.content.hashCode();
    }
}
