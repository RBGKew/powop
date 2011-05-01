package org.emonocot.model.taxon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.emonocot.model.common.Base;
import org.emonocot.model.description.Content;
import org.emonocot.model.description.Feature;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;

/**
 *
 * @author ben
 *
 */
public class Taxon extends Base {
    /**
     *
     */
    private List<Image> images = new ArrayList<Image>();

    /**
     *
     * @return a list of images of the taxon
     */
    public final List<Image> getImages() {
        return images;
    }

    /**
     *
     * @return a list of references about the taxon
     */
    public final Set<Reference> getReferences() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     *
     * @return a map of content about the taxon, indexed by the subject
     */
    public final Map<Feature,Content> getContent() {
        // TODO Auto-generated method stub
        return null;
    }
}
