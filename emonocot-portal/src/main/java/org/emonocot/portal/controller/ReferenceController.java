package org.emonocot.portal.controller;

import org.emonocot.api.ReferenceService;
import org.emonocot.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/reference")
public class ReferenceController extends
        GenericController<Reference, ReferenceService> {

    /**
     *
     */
    public ReferenceController() {
        super("reference");
    }

  /**
   *
   * @param referenceService
   *            Set the reference service
   */
  @Autowired
  public final void setReferenceService(
            final ReferenceService referenceService) {
      super.setService(referenceService);
  }
}
