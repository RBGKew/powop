package org.powo.portal.controller;

import org.powo.site.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ModelAttribute;

public class LayoutController {

  @Autowired
  @Qualifier("currentSite")
  Site site;

  @ModelAttribute("siteId")
  protected String siteId() {
    return site.siteId();
  }

  @ModelAttribute("favicon")
  protected String favicon() {
    return site.favicon();
  }

  @ModelAttribute("kewLogo")
  protected String kewLogo() {
    return site.kewLogoPath();
  }
}
