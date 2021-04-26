package org.powo.portal.view;

import org.powo.model.Image;
import org.powo.model.Taxon;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeaturedTaxon {

  private Taxon taxon;

  public Image getFeaturedImage() {
    var images = taxon.getImages();
    if (images == null) {
      return null;
    }
    if (images.size() == 0) {
      return null;
    }
    return images.get(0);
  }

}
