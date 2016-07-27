package org.emonocot.model.solr;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Taxon;

public class IdentificationKeySolrInputDocument extends BaseSolrInputDocument {
	
	private IdentificationKey key;
	
	public IdentificationKeySolrInputDocument(IdentificationKey key) {
		super(key);
		super.build();
		this.key = key;
	}

	public SolrInputDocument build() {
		StringBuilder summary = new StringBuilder().append(key.getTitle()).append(" ")
				.append(key.getCreator()).append(" ").append(key.getDescription());
		if(key.getTaxa() != null) {
			boolean first = true;
			for(Taxon t : key.getTaxa()) {
				if(first) {
					addField(sid,"taxon.order_s", t.getOrder());
					addField(sid,"taxon.subgenus_s", t.getSubgenus());
				}
				addField(sid,"taxon.family_ss", t.getFamily());
				addField(sid,"taxon.genus_ss", t.getGenus());
				addField(sid,"taxon.subfamily_ss", t.getSubfamily());
				addField(sid,"taxon.subtribe_ss", t.getSubtribe());
				addField(sid,"taxon.tribe_ss", t.getTribe());
				summary.append(" ").append(t.getClazz())
				.append(" ").append(t.getFamily())
				.append(" ").append(t.getGenus())
				.append(" ").append(t.getKingdom())
				.append(" ").append(t.getOrder())
				.append(" ").append(t.getPhylum())
				.append(" ").append(t.getSubfamily())
				.append(" ").append(t.getSubgenus())
				.append(" ").append(t.getSubtribe())
				.append(" ").append(t.getTribe());
				first = false;
			}
		}
		sid.addField("searchable.solrsummary_t", summary.toString());

		return sid;
	}
}
