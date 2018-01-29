package org.powo.api;

import org.powo.model.marshall.json.ResourceWithJob;

public interface ResourceWithJobService {

	public void save(ResourceWithJob resourceWithJob);

	public void saveOrUpdate(ResourceWithJob resourceWithJob);

}
