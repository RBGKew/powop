package org.emonocot.job.scratchpads.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.emonocot.job.scratchpads.model.EoLDataObject;
import org.emonocot.job.scratchpads.model.EoLReference;
import org.emonocot.job.scratchpads.model.EoLTaxonItem;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class EoLTaxonItemConverter implements Converter<EoLTaxonItem,Taxon> {
	
	private TaxonService taxonService;
	
	private ConversionService conversionService;	
	
	@Autowired
	public void setTaxonService(TaxonService taxonService) {
		this.taxonService = taxonService;
	}
	
	
	@Autowired
	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public Taxon convert(EoLTaxonItem input) {
		Taxon taxon = taxonService.load(input.getIdentifer());

		for (EoLDataObject dataObject : input.getDataObjects()) {
			if(dataObject.getDataType().equals("http://purl.org/dc/dcmitype/StillImage")) {
				handleImage(dataObject, taxon);
			} else if(dataObject.getDataType().equals("http://purl.org/dc/dcmitype/Text")) {
				handleText(dataObject, taxon);
			}
		}
		
		for(EoLReference reference : input.getReferences()) {
			handleReference(reference, taxon);
		}

		// Now deal with the deletes i.e. data which is in the
		// target which is not in the source

		// To avoid the ConcurrentModificationException
		List<Image> images = new ArrayList<Image>();
		Collections.copy(taxon.getImages(), images);

		for (Image image : images) {
			// Assume we have a convenience method to determine if the input
			// object contains an image using it's URL
			if (!input.containsImage(image.getURL())) {
				taxon.getImages().remove(image);
			}
		}

		// and do the same for references and for text elements, noting that the
		// semantics are slightly different - removing a reference does not delete it as
		// the taxon-reference relationship is many-to-many wheras the taxon-contentelement is
		// one-to-many thus removing a content element from its parent collection has the
		// effect of deleting it from the database (and should be mapped using orphanRemoval = true)
		
		return taxon;
	}
	
	private void handleText(EoLDataObject dataObject, Taxon taxon) {
	      // We need to know which taxon the dataObject refers in order to look it up properly
	      dataObject.setTaxon(taxon.getUuid()); 

	      TextContent text = conversionService.convert(dataObject, TextContent.class);
	      if(taxon.getContent().containsValue(text)) {
	          taxon.getContent().put(text.getFeature(), text);
	      } else {
	          taxon.getContent().put(text.getFeature(),text);
	      }
		}

		private void handleReference(EoLReference eolReference, Taxon taxon) {
			 /**
	         * see the comment below about the conversion service
	         */
	        Reference reference = conversionService.convert(eolReference, Reference.class);
	        if(taxon.getReferences().contains(reference)) {
	        	taxon.getReferences().remove(reference);
	            taxon.getReferences().add(reference);
	        } else {
	            taxon.getReferences().add(reference);
	        }
		}

		private void handleImage(EoLDataObject dataObject, Taxon taxon) {
			 /**
	         * conversion service internally calls the persistance layer and either returns
	         * a new, unpersisted Image instance if that image is unknown to eMonocot
	         * i.e. if the image has not ever been known to eMonocot, or if the image is 
	         * persisted within eMonocot, the persisted version, possibly updated to include
	         * recent changes in the DTO if the object has been changed.
	         */
	        Image image = conversionService.convert(dataObject, Image.class);
	        if(taxon.getImages().contains(image)) {
	        	int index = taxon.getImages().indexOf(image);
	        	taxon.getImages().remove(index);
	            taxon.getImages().add(index,image);
	        } else {
	            taxon.getImages().add(image);
	        }
		}

}
