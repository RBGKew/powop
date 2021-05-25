package org.powo.portal.view;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeaturedTaxaSection {

  private String title;
  private List<FeaturedTaxon> featuredTaxa;

}
