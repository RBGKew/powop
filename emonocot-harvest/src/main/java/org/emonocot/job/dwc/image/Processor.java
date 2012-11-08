package org.emonocot.job.dwc.image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.emonocot.api.ImageService;
import org.emonocot.job.dwc.NonOwnedProcessor;
import org.emonocot.model.Image;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is slightly different from the description validator because we believe
 * (or at least, I believe) that descriptive content is somehow "owned" or
 * "contained in" the taxon i.e. that if the taxon does not exist, the
 * descriptive content doesn't either. There is a one-to-many relationship
 * between taxa and descriptive content because there can be many facts about
 * each taxon, but each fact is only about one taxon.
 *
 * Images, on the other hand, have an inherently many-to-many relationship with
 * taxa as one image can appear on several different taxon pages (especially the
 * family page, type genus page, type species page etc). Equally a taxon page
 * can have many images on it. So Images don't belong to any one taxon page
 * especially. If we delete the taxon, the image can hang around - it might have
 * value on its own.
 *
 * @author ben
 *
 */
public class Processor extends NonOwnedProcessor<Image, ImageService> implements ItemWriteListener<Image> {

    private Logger logger = LoggerFactory.getLogger(Processor.class);

    @Autowired
    public final void setImageService(ImageService imageService) {
        super.service = imageService;
    }
    
    /**
     * @param images the list of images written
     */
    public void afterWrite(List<? extends Image> images) {

    }

    /**
     * @param images the list of images to write
     */
    public final void beforeWrite(final List<? extends Image> images) {
        logger.info("Before Write");
        
        Comparator<Taxon> comparator = new TaxonComparator();
        for (Image image : images) {
            if (!image.getTaxa().isEmpty()) {
            	for(Taxon taxon : image.getTaxa()) {
            		if(!taxon.getImages().contains(image)) {
            			taxon.getImages().add(image);
            		}
            	}
                if (image.getTaxa().size() == 1) {
                    image.setTaxon(image.getTaxa().iterator().next());
                } else {
                    List<Taxon> sorted = new ArrayList<Taxon>(image.getTaxa());
                    Collections.sort(sorted, comparator);
                    image.setTaxon(sorted.get(0));
                }
            }
        }
        for (Image image : images) {
            if (!image.getTaxa().isEmpty()) {
                for (Taxon t : image.getTaxa()) {
                    if (t.getImage() == null) {
                        t.setImage(t.getImages().get(0));
                    }
                }
            }
        }
    }

    /**
     * @param exception the exception thrown
     * @param images the list of images
     */
    public final void onWriteError(final Exception exception, final List<? extends Image> images) {

    }

    /**
     *
     * @author ben
     *
     */
    class TaxonComparator implements Comparator<Taxon> {

        /**
         * @param o1
         *            Set the first taxon
         * @param o2
         *            Set the second taxon
         * @return < 0 if the first should come before the second, > 0 if the
         *         first should come after and 0 if they are equal
         */
        public int compare(final Taxon o1, final Taxon o2) {
            if (o1 == o2) {
                return 0;
            }
            if (o1.getTaxonRank() == null) {
                if (o2.getTaxonRank() == null) {
                    return 0;
                }  else {
                    return 1;
                }
            } else {
              return o1.getTaxonRank().compareTo(o2.getTaxonRank());
            }
        }
    }

	@Override
	protected void doUpdate(Image persisted, Image t) {
		persisted.setAudience(t.getAudience());
		persisted.setContributor(t.getContributor());
		persisted.setCreator(t.getCreator());
		persisted.setDescription(t.getDescription());
		persisted.setFormat(t.getFormat());
		persisted.setLatitude(t.getLatitude());
		persisted.setLongitude(t.getLongitude());
		persisted.setReferences(t.getReferences());
		persisted.setSpatial(t.getSubject());
		persisted.setTaxon(t.getTaxon());
		persisted.setTitle(t.getTitle());
	}

	@Override
	protected void doPersist(Image t) {
		if(!t.getTaxa().isEmpty()) {
			t.setTaxon(t.getTaxa().iterator().next());
		}
	}

	@Override
	protected RecordType getRecordType() {
		return RecordType.Image;
	}

	@Override
	protected void bind(Image t) {
		boundObjects.put(t.getIdentifier(), t);
	}

	@Override
	protected Image retrieveBound(Image t) {
		return service.find(t.getIdentifier());
	}

	@Override
	protected Image lookupBound(Image t) {
		return boundObjects.get(t.getIdentifier());
	}

	@Override
	protected void doValidate(Image t) throws Exception {
		
	}

}
