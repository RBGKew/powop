package org.tdwg.voc;

/**
 *
 * @author ben
 *
 */
public class PublicationCitation extends org.tdwg.PublicationCitation {

    /**
     *
     */
    private String tpubTitle;

    /**
     *
     */
    private String tpubAuthorship;

    /**
     *
     */
    private String tpubPublisher;

    /**
     *
     */
    private ParentPublication tpubParentPublication;

    /**
     *
     */
    private DefinedTermLinkType tpubPublicationType;

    /**
     *
     */
    private String tpubVolume;

    /**
     *
     */
    private String tpubPages;

    /**
     *
     */
    private String tpubDatePublished;

    /**
     * @return the title
     */
    public final String getTpubTitle() {
        return tpubTitle;
    }

    /**
     * @param title the title to set
     */
    public final void setTpubTitle(final String title) {
        this.tpubTitle = title;
    }

    /**
     * @return the authorship
     */
    public final String getAuthorship() {
        return tpubAuthorship;
    }

    /**
     * @param authorship the authorship to set
     */
    public final void setAuthorship(final String authorship) {
        this.tpubAuthorship = authorship;
    }

    /**
     * @return the publisher
     */
    public final String getPublisher() {
        return tpubPublisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public final void setPublisher(final String publisher) {
        this.tpubPublisher = publisher;
    }

    /**
     * @return the tpubParentPublication
     */
    public final PublicationCitation getParentPublication() {
        if (tpubParentPublication == null) {
            return null;
        } else {
            return tpubParentPublication.getPublicationCitation();
        }
    }

    /**
     * @param parentPublication the parentPublication to set
     */
    public final void setParentPublication(
            final PublicationCitation parentPublication) {
        if (parentPublication == null) {
            tpubParentPublication = null;
        } else {
            tpubParentPublication = new ParentPublication(parentPublication);
        }
    }

    /**
     * @return the publicationType
     */
    public final PublicationTypeTerm getPublicationType() {
        if (tpubPublicationType == null) {
            return null;
        } else {
            return (PublicationTypeTerm) tpubPublicationType.getDefinedTerm();
        }
    }

    /**
     * @param publicationType the publicationType to set
     */
    public final void setPublicationType(
            final PublicationTypeTerm publicationType) {
        if (publicationType != null) {
            this.tpubPublicationType
                = new DefinedTermLinkType(publicationType, true);
        } else {
            this.tpubPublicationType = null;
        }
    }

    /**
     * @return the volume
     */
    public final String getVolume() {
        return tpubVolume;
    }

    /**
     * @param volume set the volume
     */
    public final void setVolume(final String volume) {
        this.tpubVolume = volume;
    }

    /**
     * @return the pages
     */
    public final String getPages() {
        return tpubPages;
    }

    /**
     * @param pages set the pages
     */
    public final void setPages(final String pages) {
        this.tpubPages = pages;
    }

    /**
     * @return the date published
     */
    public final String getDatePublished() {
        return tpubDatePublished;
    }

    /**
     * @param datePublished set the date published
     */
    public final void setDatePublished(final String datePublished) {
        this.tpubDatePublished = datePublished;
    }
}
