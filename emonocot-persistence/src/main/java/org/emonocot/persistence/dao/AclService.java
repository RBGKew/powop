package org.emonocot.persistence.dao;

import java.util.List;

import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAclService;

/**
 *
 * @author ben
 *
 */
public interface AclService extends MutableAclService {

    /**
     *
     * @param sid The Security ID
     * @return a list of arrays  of SecuredObject, ACE
     */
    List<Object[]> listAces(PrincipalSid sid);

}
