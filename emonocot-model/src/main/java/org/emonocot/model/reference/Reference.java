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
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     *
     * @param newVolume set the volume
     */
    public void setVolume(String newVolume) {
        this.volume = newVolume;
    }

    /**
     *
     * @param newPages set the pages
     */
    public void setPages(String newPages) {
        this.pages = newPages;
    }

    /**
     *
     * @param newDatePublished set the date published
     */
    public void setDate(String newDatePublished) {
        this.datePublished = newDatePublished;
    }

    /**
     *
     * @param newReferenceType set the reference type
     */
    public void setType(ReferenceType newReferenceType) {
        this.type = newReferenceType;
    }

    /**
     * @return the datePublished
     */
    @Field
    public String getDatePublished() {
        return datePublished;
    }

    /**
     * @param newDatePublished the datePublished to set
     */
    public void setDatePublished(String newDatePublished) {
        this.datePublished = newDatePublished;
    }

    /**
     * @return the title
     */
    @Field
    public String getTitle() {
        return title;
    }

    /**
     * @return the volume
     */
    @Field
    public String getVolume() {
        return volume;
    }

    /**
     * @return the pages
     */
    @Field
    public String getPages() {
        return pages;
    }

    /**
     * @return the type
     */
    public ReferenceType getType() {
        return type;
    }
}
