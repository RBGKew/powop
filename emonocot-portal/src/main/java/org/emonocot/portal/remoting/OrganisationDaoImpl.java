package org.emonocot.portal.remoting;

import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.emonocot.model.registry.Organisation;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.OrganisationDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author annapaola
 *
 */
@Repository
public class OrganisationDaoImpl extends DaoImpl<Organisation> implements OrganisationDao {
   /**
    *
    */
   public OrganisationDaoImpl() {
       super(Organisation.class, "organisation");
   }

@Override
public Page<SolrDocument> searchForDocuments(String query, Integer pageSize,
		Integer pageNumber, Map<String, String> selectedFacets, String sort) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Organisation loadObjectForDocument(SolrDocument solrDocument) {
	// TODO Auto-generated method stub
	return null;
}

}
