package org.emonocot.service.impl;

import org.emonocot.api.GroupService;
import org.emonocot.model.user.Group;
import org.emonocot.persistence.dao.GroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class GroupServiceImpl extends ServiceImpl<Group,GroupDao> implements GroupService {
    
    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        super.dao = groupDao;
    }
    
}
