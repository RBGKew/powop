package org.powo.portal.controller;

import org.powo.site.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;

public class HtmlController {

  @Autowired
  @Qualifier("currentSite")
  Site site;

  /**
   * Populate universal attributes on the model, required by any
   * html view.
   *
   * @param model
   */
  protected void initialiseModel(Model model) {
    model.addAttribute("siteId", site.siteId());
    model.addAttribute("kew-logo", site.kewLogoPath());
    model.addAttribute("favicon", site.favicon());
  }

}
