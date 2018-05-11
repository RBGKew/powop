package org.powo.model.solr;

import org.apache.solr.common.SolrInputDocument;
import org.powo.model.Image;

public class ImageSolrInputDocument extends BaseSolrInputDocument {

	private Image image;
	
	public ImageSolrInputDocument(Image image) {
		super(image);
		super.build();
		this.image = image;
	}
	
	public SolrInputDocument build() {
		StringBuilder summary = new StringBuilder().append(image.getAudience())
				.append(" ").append(image.getCreator()).append(" ")
				.append(image.getDescription()).append(" ").append(image.getPublisher())
				.append(" ").append(image.getReferences()).append(" ")
				.append(image.getSubject()).append(" ").append(image.getTitle()).append(" ");
		if(image.getTaxon() != null) {
			addField(sid,"taxon.family_ss", image.getTaxon().getFamily());
			addField(sid,"taxon.genus_ss", image.getTaxon().getGenus());
			addField(sid,"taxon.order_s", image.getTaxon().getOrder());
			addField(sid,"taxon.subfamily_ss", image.getTaxon().getSubfamily());
			addField(sid,"taxon.subgenus_s", image.getTaxon().getSubgenus());
			addField(sid,"taxon.subtribe_ss", image.getTaxon().getSubtribe());
			addField(sid,"taxon.tribe_ss", image.getTaxon().getTribe());
			summary.append(" ").append(image.getTaxon().getClazz())
			.append(" ").append(image.getTaxon().getClazz())
			.append(" ").append(image.getTaxon().getFamily())
			.append(" ").append(image.getTaxon().getGenus())
			.append(" ").append(image.getTaxon().getKingdom())
			.append(" ").append(image.getTaxon().getOrder())
			.append(" ").append(image.getTaxon().getPhylum())
			.append(" ").append(image.getTaxon().getSubfamily())
			.append(" ").append(image.getTaxon().getSubgenus())
			.append(" ").append(image.getTaxon().getSubtribe());
		}
		sid.addField("searchable.solrsummary_t", summary.toString());
		return sid;
	}
}