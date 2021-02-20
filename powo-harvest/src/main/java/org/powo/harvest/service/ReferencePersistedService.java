package org.powo.harvest.service;

import org.powo.model.Reference;
import org.springframework.stereotype.Repository;

@Repository
public class ReferencePersistedService extends PersistedService<Reference> {
    
    public ReferencePersistedService() {
        super(Reference.class);
    }

}
