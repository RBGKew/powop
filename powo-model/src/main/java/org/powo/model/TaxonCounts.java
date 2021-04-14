package org.powo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaxonCounts {

  private Long totalCount;
  private Integer speciesCount;
  private Integer genusCount;
  private Integer familyCount;

}
