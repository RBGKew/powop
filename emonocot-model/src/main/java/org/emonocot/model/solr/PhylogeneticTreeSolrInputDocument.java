package org.emonocot.model.solr;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.PhylogeneticTree;
import org.emonocot.model.Taxon;

public class PhylogeneticTreeSolrInputDocument extends BaseSolrInputDocument {

	private PhylogeneticTree tree;

	public PhylogeneticTreeSolrInputDocument(PhylogeneticTree tree) {
		super(tree);
		super.build();
		this.tree = tree;
	}
	
	public SolrInputDocument build() {
		StringBuilder summary = new StringBuilder().append(tree.getTitle()).append(" ")
				.append(tree.getCreator()).append(" ").append(tree.getDescription());
		if(tree.getTaxa() != null) {
			boolean first = true;
			for(Taxon t : tree.getTaxa()) {
				if(first) {
					addField(sid,"taxon.subgenus_s", t.getSubgenus());
					addField(sid,"taxon.order_s", t.getOrder());
				}
				addField(sid,"taxon.family_ss", t.getFamily());
				addField(sid,"taxon.genus_ss", t.getGenus());
				addField(sid,"taxon.subfamily_ss", t.getSubfamily());
				addField(sid,"taxon.subtribe_ss", t.getSubtribe());
				addField(sid,"taxon.tribe_ss", t.getTribe());
				summary.append(" ").append(t.getClazz())
				.append(" ").append(t.getClazz())
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
