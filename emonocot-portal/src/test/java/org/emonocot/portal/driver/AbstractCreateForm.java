package org.emonocot.portal.driver;

import org.emonocot.model.common.Base;

public abstract class AbstractCreateForm extends
		AbstractUpdateForm {

	/**
	 *
	 */
	private String objectIdentifier;


	/**
	 *
	 * @return the current page
	 */
	@Override
	public final PageObject submit() {
	    getForm().submit();
	    if (objectIdentifier != null) {
	            Base t;
				try {
					t = objectClass.newInstance();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
	            t.setIdentifier(objectIdentifier);
	            testDataManager.registerObject(t);
	            this.objectIdentifier = null;
	    }
	    return getPage(onSubmitPageClass);
	}

	/**
	 *
	 * @param fieldValue Set the identifier
	 * @param fieldName TODO
	 */
	public final void setObjectIdentifier(final String fieldValue, String fieldName) {
	    objectIdentifier = fieldValue;
	    super.setFormField(fieldName, fieldValue);
	}

}