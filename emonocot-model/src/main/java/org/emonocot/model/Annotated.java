package org.emonocot.model;

import java.util.Set;

public interface Annotated {
	
	Set<Annotation> getAnnotations();
	
	void setAnnotations(Set<Annotation> annotations);

}
