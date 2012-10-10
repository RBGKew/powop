package org.emonocot.harvest.common;

import java.util.List;

import org.emonocot.model.Base;
import org.springframework.batch.item.ItemWriter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateDeletingWriter extends HibernateDaoSupport implements
		ItemWriter<Base> {

	@Override
	public void write(List<? extends Base> items) throws Exception {
		getHibernateTemplate().deleteAll(items);
		getHibernateTemplate().flush();		
	}

}
