package org.emonocot.persistence.dao.hibernate;

import org.emonocot.model.description.Content;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.DescriptionDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class DescriptionDaoImpl extends DaoImpl<Content> implements
        DescriptionDao {

    @Override
    public final TextContent getTextContent(
            final Feature feature, final Taxon taxon) {
        // TODO Auto-generated method stub
        return null;
    }

}
