package org.powo.portal.view;

import org.powo.model.Image;
import org.powo.model.Taxon;
import org.springframework.context.MessageSource;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeaturedTaxon {

  private Taxon taxon;
  private MessageSource messageSource;

  public Image getFeaturedImage() {
    if (taxon == null) {
      return null;
    }
    var images = taxon.getImages();
    if (images == null) {
      return null;
    }
    if (images.size() == 0) {
      return null;
    }
    return images.get(0);
  }

  /**
   * Return the summary for this taxon, as a string.
   * 
   * @return
   */
  public String getSummary() {
    if (taxon == null) {
      return "";
    }
    return new Summary(taxon, messageSource).build();
  }

}
