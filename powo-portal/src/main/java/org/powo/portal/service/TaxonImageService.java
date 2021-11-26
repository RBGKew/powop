package org.powo.portal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.powo.api.ImageService;
import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.model.helpers.CDNImageHelper;
import org.powo.portal.view.TaxonImage;
import org.powo.portal.view.TaxonImageSet;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaxonImageService {
	private final ImageService imageService;
	private final CDNImageHelper cdnImageHelper;
	private final ImageCaptionService captionService;

	public TaxonImageSet getTaxonImageSet(Taxon taxon) {
		var imageSet = new TaxonImageSet();
		var images = taxon.looksAccepted() ? availableImages(taxon) : List.<Image>of();

		imageSet.setImages(
			images.stream().map(i -> toTaxonImage(i, taxon)).collect(Collectors.toList())
		);

		if (images.size() > 0) {
			imageSet.setHeaderImage(toTaxonImage(images.get(0), taxon));
		}

		for (var img : images) {
			imageSet.getSources().add(img.getAuthority());
		}

		return imageSet;
	}

	private List<Image> availableImages(Taxon taxon) {
		var images = new ArrayList<Image>();
		images.addAll(taxon.getImages());

		for (Taxon synonym : taxon.getSynonymNameUsages()) {
			images.addAll(synonym.getImages());
		}

		if (images.size() < 6) {
			var topImages = imageService.getTopImages(taxon, 6 - images.size());
			images.addAll(topImages);
		}

		return images;
	}

	private TaxonImage toTaxonImage(Image image, Taxon taxon) {
		return TaxonImage.builder()
			.fullsizeUrl(cdnImageHelper.getFullsizeUrl(image))
			.thumbnailUrl(cdnImageHelper.getThumbnailUrl(image))
			.caption(captionService.getHtmlCaption(image, taxon))
			.build();
	}
}
