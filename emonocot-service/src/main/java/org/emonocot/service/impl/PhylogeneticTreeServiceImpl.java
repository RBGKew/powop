/**
 *
 */
package org.emonocot.service.impl;

import org.emonocot.api.PhylogeneticTreeService;
import org.emonocot.model.PhylogeneticTree;
import org.emonocot.persistence.dao.PhylogeneticTreeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ben
 */
@Service
public class PhylogeneticTreeServiceImpl extends ServiceImpl<PhylogeneticTree, PhylogeneticTreeDao>  implements PhylogeneticTreeService {

    @Autowired
    public void setPhylogeneticTreeDao(PhylogeneticTreeDao dao) {
        super.dao = dao;
    }
}
