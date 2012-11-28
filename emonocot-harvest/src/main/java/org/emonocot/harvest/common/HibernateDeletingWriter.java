package org.emonocot.harvest.common;

import java.util.List;

import org.emonocot.model.Base;
import org.springframework.batch.item.ItemWriter;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateDeletingWriter<T extends Base> extends HibernateDaoSupport implements
		ItemWriter<T> {

	@Override
	public void write(List<? extends T> items) throws Exception {
		getHibernateTemplate().deleteAll(items);
	}

}
