package org.emonocot.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author ben
 *
 */
@MappedSuperclass
public abstract class SearchableObject extends BaseData {

    /**
     *
     */
    private static final long serialVersionUID = 4960825789689641206L;

    /**
     *
     * @return the simple name of the implementing class
     */
    @Transient
    @JsonIgnore
    public String getClassName() {
        return getClass().getSimpleName();
    }

	public SolrInputDocument toSolrInputDocument() {
		SolrInputDocument sid = new SolrInputDocument();
		sid.addField("base.access_rights_s", getAccessRights());
		DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.basicDateTimeNoMillis();
		if(getCreated() != null) {
		    sid.addField("base.created_dt", dateTimeFormatter.print(getCreated()));
		}
		if(getModified() != null) {
		    sid.addField("base.modified_dt", dateTimeFormatter.print(getModified()));
		}
		sid.addField("base.license_s", getLicense());
		sid.addField("base.rights_s", getRights());
		sid.addField("base.rights_holder_s", getRightsHolder());
		if(getAuthority() != null) {
			sid.addField("base.authority_s", getAuthority().getIdentifier());
		}
		return sid;
	}

}
