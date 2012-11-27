package org.emonocot.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class OwnedEntity extends BaseData implements Owned {

}
