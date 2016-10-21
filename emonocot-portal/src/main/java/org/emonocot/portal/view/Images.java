package org.emonocot.portal.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.emonocot.model.Image;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.registry.Organisation;

import com.google.common.collect.ImmutableList;

public class Images {

	Taxon taxon;
	List<Image> images;
	Set<Organisation> sources;

	private static final ImmutableList<DescriptionType> descriptionOrder = ImmutableList.<DescriptionType>builder()
		.add(DescriptionType.general)
		.add(DescriptionType.habit)
		.add(DescriptionType.morphologyReproductiveFlowers)
		.add(DescriptionType.morphologyReproductiveFruits)
		.add(DescriptionType.morphologyReproductiveInflorescences)
		.build();
	
	
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

	@SuppressWarnings("unchecked")
	public List<Image> getHeaderImages() {

		Comparator<Image> byType = new Comparator<Image>() {
        			
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
        Comparator<Image> byRating = new Comparator<Image>() {
        		
	            @Override
	            public int compare(Image o1, Image o2) {
	            	if(o2.getRating() != null && o1.getRating() != null){
	                return Double.compare(o2.getRating(), o1.getRating());
		            }if(o2.getRating() != null){
		            	return 1;
		            }if(o2.getRating() != null){
		            	return -1;
		            }
	            	return 0;
	            }
        	};
        	
        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(byType);
        chain.addComparator(byRating);
        List<Image> sortedImages = new ArrayList<Image>(images);
        Collections.sort(sortedImages, chain);
		return sortedImages.subList(0, Math.min(images.size(), 1));
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
