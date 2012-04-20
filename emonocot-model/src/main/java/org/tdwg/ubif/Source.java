package org.tdwg.ubif;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author ben
 *
 */
public class Source {
    /**
     *
     */
   @XStreamAsAttribute
   private String href;

    /**
     * @return the href
     */
    public final String getHref() {
        return href;
    }

    /**
     * @param newHref the href to set
     */
    public final void setHref(final String newHref) {
        this.href = newHref;
    }

}
