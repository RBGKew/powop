package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.emonocot.model.Image;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.registry.Organisation;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

public class Images {

	Taxon taxon;
	List<Image> images;
	Set<Organisation> sources;

	private static final ImmutableList<DescriptionType> descriptionOrder = ImmutableList.<DescriptionType>builder()
			.add(DescriptionType.general)
			.add(DescriptionType.habit)
			.add(DescriptionType.morphologyReproductiveFlower)
			.add(DescriptionType.morphologyReproductiveFruit)
			.add(DescriptionType.morphologyReproductiveInflorescence)
			.build();

	private static final Comparator<Image> byType = new Comparator<Image>() {
			@Override
			public int compare(Image o1, Image o2) {
				int o1Rank = 0;
				int o2Rank = 0;
				for(DescriptionType type : o1.getSubjectPart()){
					if(descriptionOrder.contains(type) && descriptionOrder.indexOf(o1) < o1Rank ){
						o1Rank = descriptionOrder.indexOf(type);
					}
				}
				for(DescriptionType type : o2.getSubjectPart()){
					if(descriptionOrder.contains(type) && descriptionOrder.indexOf(type) < o1Rank ){
						o2Rank = descriptionOrder.indexOf(type);
					}
				}
				return Integer.compare(o2Rank, o1Rank);
			}
		};

	private static final Ordering<Image> byRating = Ordering.natural()
			.reverse()
			.nullsLast()
			.onResultOf(new Function<Image, Double>() {
				@Override public Double apply(Image image) {
					return image.getRating();
				}
			});

	public Images(Taxon taxon) {
		this.taxon = taxon;
		if(taxon.isAccepted()) {
			images = taxon.getImages();
			for(Taxon synonym : taxon.getSynonymNameUsages()) {
				images.addAll(synonym.getImages());
			}
		} else {
			images = new ArrayList<>();
		}
	}

	public List<Image> getHeaderImages() {
		return byRating
				.sortedCopy(images)
				.subList(0, Math.min(images.size(), 1));
	}

	public List<Image> getAll() {
		return images;
	}

	public Set<Organisation> getSources() {
		if(sources == null) {
			sources = new HashSet<>();
			for(Image img : images) {
				sources.add(img.getAuthority());
			}
		}

		return sources;
	}
}
