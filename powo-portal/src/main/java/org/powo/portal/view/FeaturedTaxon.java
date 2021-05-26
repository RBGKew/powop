package org.powo.portal.view;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
   * Return the taxon descriptions, sorted to display `priorityDescriptionType`
   * first.
   * 
   * @return
   */
  public List<Description> getDescriptions() {
    if (taxon == null) {
      return null;
    }
    var descriptions = Lists.newArrayList(taxon.getDescriptions());
    if (priorityDescriptionType == null) {
      return descriptions.stream()
      .sorted(Comparator.comparing((Description d) -> d.getType().name()))
      .collect(Collectors.toList());
    }
    var priorityDescriptionTypes = DescriptionType.getAll(priorityDescriptionType);
    /**
     * The first comparison is reversed, because items where contains() -> TRUE go to the end of the list,
     * but we want them to be at the start.
     */
    return descriptions.stream()
        .sorted(Comparator.comparing((Description d) -> priorityDescriptionTypes.contains(d.getType())).reversed()
            .thenComparing((Description d) -> d.getType().name()))
        .collect(Collectors.toList());
  }

}
