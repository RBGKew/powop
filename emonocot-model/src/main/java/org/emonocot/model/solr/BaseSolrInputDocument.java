package org.emonocot.model.solr;

import java.io.Serializable;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.SearchableObject;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class BaseSolrInputDocument {
	protected SolrInputDocument sid;
	private SearchableObject obj;
	private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	public BaseSolrInputDocument(SearchableObject obj) {
		this.sid = new SolrInputDocument();
		this.obj = obj;
	}
	
	public SolrInputDocument build() {
		sid.addField("id", obj.getDocumentId());
		sid.addField("base.id_l", obj.getId());
		sid.addField("base.class_searchable_b", true);
		sid.addField("base.class_s", obj.getClass().getName());
		addField(sid,"base.access_rights_s", obj.getAccessRights());

		if(obj.getCreated() != null) {
			sid.addField("base.created_dt", dateTimeFormatter.print(obj.getCreated()));
		}
		if(obj.getModified() != null) {
			sid.addField("base.modified_dt", dateTimeFormatter.print(obj.getModified()));
		}
		addField(sid,"base.license_s", obj.getLicense());
		addField(sid,"base.rights_s", obj.getRights());
		addField(sid,"base.rights_holder_s", obj.getRightsHolder());
		if(obj.getAuthority() != null) {
			sid.addField("base.authority_s", obj.getAuthority().getIdentifier());
			sid.addField("searchable.sources_ss", obj.getAuthority().getIdentifier());
		}
		return sid;
	}

	protected void addField(SolrInputDocument sid, String name, Serializable value) {
		if(value != null && !value.toString().isEmpty()) {
			sid.addField(name, value);
		}
	}
}
