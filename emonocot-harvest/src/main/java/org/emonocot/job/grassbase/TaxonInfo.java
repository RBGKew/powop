package org.emonocot.job.grassbase;

/**
 *
 * @author ben
 *
 */
public class TaxonInfo {

   /**
    *
    */
    private Integer checklistId;
   /**
    *
    */
    private String name;
   /**
    *
    */
    private String link;

   /**
    *
    */
    private Integer itemId;

    /**
     *
     * @param newChecklistId
     *            Set the checklist identifier
     * @param newName
     *            Set the name
     * @param newLink
     *            Set the link
     */
    public TaxonInfo(final Integer newChecklistId, final String newName,
            final String newLink) {
        this.checklistId = newChecklistId;
        this.name = newName;
        this.link = newLink;
    }

    /**
     * @return the checklistId
     */
    public final Integer getChecklistId() {
        return checklistId;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @return the link
     */
    public final String getLink() {
        return link;
    }

    /**
     * @return the item id
     */
    public final Integer getItemId() {
        return itemId;
    }

    /**
     * @param newItemId the item id to set
     */
    public final void setItemId(final Integer newItemId) {
        this.itemId = newItemId;
    }
}
