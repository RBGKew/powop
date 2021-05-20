package org.powo.portal.view;

import java.util.List;

import com.google.common.collect.Lists;

import org.powo.model.Description;
import org.powo.model.Image;
import org.powo.model.Taxon;
import org.powo.model.constants.DescriptionType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeaturedTaxon {

  private Taxon taxon;
  private DescriptionType priorityDescriptionType;

  public FeaturedTaxon(Taxon taxon) {
    this.taxon = taxon;
  }

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
   * Return the taxon descriptions, sorted to display uses first.
   * @return
   */
  public List<Description> getDescriptions() {
    if (taxon == null) {
      return null;
    }
    var descriptions = Lists.newArrayList(taxon.getDescriptions());
    if (priorityDescriptionType == null) {
      return descriptions;
    }
    var priorityDescriptionTypes = DescriptionType.getAll(priorityDescriptionType);
    descriptions.sort((Description d1, Description d2) -> {
      if (priorityDescriptionTypes.contains(d1.getType()) && !priorityDescriptionTypes.contains(d2.getType())) {
        return -1;
      }
      if (!priorityDescriptionTypes.contains(d1.getType()) && priorityDescriptionTypes.contains(d2.getType())) {
        return 1;
      }
      return 0;
    });
    return descriptions;
  }

}
