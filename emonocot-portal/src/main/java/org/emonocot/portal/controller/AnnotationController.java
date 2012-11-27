package org.emonocot.portal.controller;

import org.emonocot.api.AnnotationService;
import org.emonocot.model.Annotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/annotation")
public class AnnotationController extends
        GenericController<Annotation, AnnotationService> {

    /**
     *
     */
   public AnnotationController() {
        super("annotation");
    }

    /**
     * @param newService
     *            set the annotation service
     */
    @Autowired
    public final void setAnnotationService(final AnnotationService newService) {
        super.setService(newService);
    }
}
