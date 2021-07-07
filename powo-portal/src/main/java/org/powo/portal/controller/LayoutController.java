package org.powo.portal.controller;

import org.powo.site.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Parent controller for HTML pages using the standard layout and design.
 * 
 * Uses @ModelAttribute annotations to add properties used in identifying the
 * current site and rendering the page layout.
 */
public class LayoutController {

  @Autowired
  @Qualifier("currentSite")
  Site site;

  @ModelAttribute("siteId")
  protected String siteId() {
    return site.siteId();
  }

  @ModelAttribute("oneTrustID")
  protected String oneTrustID() {
    return site.oneTrustID();
  }
  @ModelAttribute("canonicalUrl")
  protected String canonicalUrl() {
    return site.canonicalUrl();
  }

  @ModelAttribute("siteIdCapitlized")
  protected String siteIdCapitlized() {
    return site.siteIdCapitlized();
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
