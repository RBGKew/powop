package org.tdwg.ubif;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.tdwg.ubif.marshall.xml.DateTimeConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 *
 * @author ben
 *
 */
public class RevisionData {

    /**
     *
     */
    @XStreamAlias("Creators")
    private List<Agent> creators = new ArrayList<Agent>();

    /**
     *
     */
    @XStreamAlias("DateCreated")
    @XStreamConverter(DateTimeConverter.class)
    private DateTime dateCreated;

    /**
     * @return the creators
     */
    public final List<Agent> getCreators() {
        return creators;
    }

    /**
     * @param newCreators the creators to set
     */
    public final void setCreators(final List<Agent> newCreators) {
        this.creators = newCreators;
    }

    /**
     * @return the dateCreated
     */
    public final DateTime getDateCreated() {
        return dateCreated;
    }

    /**
     * @param newDateCreated the dateCreated to set
     */
    public final void setDateCreated(final DateTime newDateCreated) {
        this.dateCreated = newDateCreated;
    }

}
