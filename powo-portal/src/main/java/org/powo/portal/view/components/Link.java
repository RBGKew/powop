package org.powo.portal.view.components;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a link (<a>) for use in handlebars templates
 */
@Data
@AllArgsConstructor
public class Link {
    private String href;
    private String text;
}
