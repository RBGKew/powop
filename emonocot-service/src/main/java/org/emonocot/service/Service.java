package org.emonocot.service;

import org.emonocot.model.common.Base;

public interface Service<T extends Base> {	
	T load(String identifer);
}
