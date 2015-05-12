/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
