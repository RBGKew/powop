package org.emonocot.portal.controller;

import org.emonocot.api.TypeAndSpecimenService;
import org.emonocot.model.TypeAndSpecimen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/typeAndSpecimen")
public class TypeAndSpecimenController extends
		GenericController<TypeAndSpecimen, TypeAndSpecimenService> {
	
	private static final Logger logger = LoggerFactory.getLogger(TypeAndSpecimenController.class);

	public TypeAndSpecimenController() {
		super("typeAndSpecimen",TypeAndSpecimen.class);
	}
	
	@Autowired
    public void setTypeAndSpecimenService(TypeAndSpecimenService typeAndSpecimenService) {
        super.setService(typeAndSpecimenService);
    }
}
