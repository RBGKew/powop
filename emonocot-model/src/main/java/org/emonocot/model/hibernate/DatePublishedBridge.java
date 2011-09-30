package org.emonocot.model.hibernate;

import org.hibernate.search.bridge.StringBridge;

/**
 *
 * @author ben
 *
 */
public class DatePublishedBridge implements StringBridge {

    /**
     * @param o set the object
     * @return the object as a string
     */
    public final String objectToString(final Object o) {
        if (o == null) {
            return null;
        }
        if (o.getClass().equals(String.class)) {
            String string = (String) o;
            if (string.startsWith("(")) {
                string = string.substring(1);
            }
            if (string.endsWith(")")) {
                string = string.substring(0, string.length() - 1);
            }
            try {
                Integer.parseInt(string);
                return string;
            } catch (NumberFormatException nfe) {
                return null;
            }
        } else {
          return o.toString();
        }
    }

}
