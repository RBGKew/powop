package org.emonocot.portal.controller;

import org.emonocot.api.PlaceService;
import org.emonocot.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
        super("place", Place.class);
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
