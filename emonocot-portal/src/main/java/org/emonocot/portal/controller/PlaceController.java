package org.emonocot.portal.controller;

import org.emonocot.api.PlaceService;
import org.emonocot.api.TaxonService;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Place;
import org.emonocot.api.IdentificationKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author jk00kg
 */
@Controller
@RequestMapping("/place")
public class PlaceController extends
        GenericController<Place, PlaceService> {

    /**
     *
     */
    public PlaceController() {
        super("place");
    }

    /**
     * @param newIdentificationKeyService
     *            Set the identification key service
     */
    @Autowired
    public final void setPlaceService(
            final PlaceService placeService) {
        super.setService(placeService);
    }


}
