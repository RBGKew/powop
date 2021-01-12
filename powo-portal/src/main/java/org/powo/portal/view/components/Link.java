package org.powo.portal.view.components;

/**
 * Represents a link (<a>) for use in handlebars templates
 */
public class Link {

    private String href;
    private String text;

    public Link(String href, String text) {
        this.href = href;
        this.text = text;
    }

    public String getHref() {
        return href;
    }

    public String getText() {
        return text;
    }

}
