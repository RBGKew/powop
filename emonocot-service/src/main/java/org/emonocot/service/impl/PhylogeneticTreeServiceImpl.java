/**
 *
 */
package org.emonocot.service.impl;

import org.emonocot.api.PhylogeneticTreeService;
import org.emonocot.model.PhylogeneticTree;
import org.emonocot.persistence.dao.PhylogeneticTreeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ben
 */
@Service
public class PhylogeneticTreeServiceImpl extends ServiceImpl<PhylogeneticTree, PhylogeneticTreeDao>  implements PhylogeneticTreeService {

    @Autowired
    public void setPhylogeneticTreeDao(PhylogeneticTreeDao dao) {
        super.dao = dao;
    }
    
    @Override
	@Transactional(readOnly = false)
	@PreAuthorize("hasRole('PERMISSION_ADMINISTRATE') or hasRole('PERMISSION_DELETE_PHYLOGENY')")
	public void deleteById(Long id) {		
		super.deleteById(id);
	}
}
