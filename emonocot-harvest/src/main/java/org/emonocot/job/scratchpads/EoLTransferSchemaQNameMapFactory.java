package org.emonocot.job.scratchpads;

import javax.xml.namespace.QName;

import org.emonocot.job.scratchpads.model.EoLDataObject;
import org.emonocot.job.scratchpads.model.EoLTaxonItem;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import com.thoughtworks.xstream.io.xml.QNameMap;

public class EoLTransferSchemaQNameMapFactory extends AbstractFactoryBean<QNameMap> {

	/**
	 * Returns a QNameMap which maps the namespace:name couplets on 
	 * to object types for the EoLTransferSchema
	 */
	@Override
	protected QNameMap createInstance() throws Exception {
		QNameMap qNameMap = new QNameMap();
		qNameMap.registerMapping(new QName("http://www.eol.org/transfer/content/0.3","taxon"), EoLTaxonItem.class);
		qNameMap.registerMapping(new QName("http://www.eol.org/transfer/content/0.3","dataObject"), EoLDataObject.class);
		// TODO register other mappings
		return qNameMap;
	}

	@Override
	public Class<?> getObjectType() {
		return QNameMap.class;
	}

}
