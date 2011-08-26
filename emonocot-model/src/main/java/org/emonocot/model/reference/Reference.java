package org.emonocot.model.reference;

import javax.persistence.Entity;

import org.emonocot.model.common.Base;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed
public class Reference extends Base {

    /**
     *
     */
    private String title;

    /**
     *
     */
    private String volume;

    /**
     *
     */
    private String pages;

    /**
     *
     */
    private String datePublished;

    /**
     *
     */
    private ReferenceType type;

    /**
     *
     * @param newTitle set the title
     */
    public final void setTitle(final String newTitle) {
        this.title = newTitle;
    }

    /**
     *
     * @param newVolume set the volume
     */
    public final void setVolume(final String newVolume) {
        this.volume = newVolume;
    }

    /**
     *
     * @param newPages set the pages
     */
    public final void setPages(final String newPages) {
        this.pages = newPages;
    }

    /**
     *
     * @param newDatePublished set the date published
     */
    public final void setDate(final String newDatePublished) {
        this.datePublished = newDatePublished;
    }

    /**
     *
     * @param newReferenceType set the reference type
     */
    public final void setType(final ReferenceType newReferenceType) {
        this.type = newReferenceType;
    }

    /**
     * @return the datePublished
     */
    @Field
    public final String getDatePublished() {
        return datePublished;
    }

    /**
     * @param newDatePublished the datePublished to set
     */
    public final void setDatePublished(final String newDatePublished) {
        this.datePublished = newDatePublished;
    }

    /**
     * @return the title
     */
    @Field
    public final String getTitle() {
        return title;
    }

    /**
     * @return the volume
     */
    @Field
    public final String getVolume() {
        return volume;
    }

    /**
     * @return the pages
     */
    @Field
    public final String getPages() {
        return pages;
    }

    /**
     * @return the type
     */
    public final ReferenceType getType() {
        return type;
    }
}
