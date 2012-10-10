package org.emonocot.service.impl;

import java.util.List;

import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.User;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.core.Authentication;

/**
 *
 * @author ben
 *
 */
public class GroupSidRetrievalStrategyImpl extends SidRetrievalStrategyImpl {

    /**
     * @param authentication Set the authentication
     * @return the list of sids
     */
    @Override
    public final List<Sid> getSids(final Authentication authentication) {
        List<Sid> sids = super.getSids(authentication);

        if (authentication.getPrincipal() != null
                && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            for (Group g : user.getGroups()) {
                sids.add(new PrincipalSid(g.getIdentifier()));
            }
        }
        return sids;
    }

}
