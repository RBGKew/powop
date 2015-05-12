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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.emonocot.api.AnnotationService;
import org.emonocot.api.GroupService;
import org.emonocot.api.ImageService;
import org.emonocot.api.OrganisationService;
import org.emonocot.api.TaxonService;
import org.emonocot.api.UserService;
import org.emonocot.model.Annotation;
import org.emonocot.model.Image;
import org.emonocot.model.Taxon;
import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.User;
import org.emonocot.model.registry.Organisation;
import org.emonocot.test.DataManagementSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/spring/applicationContext*.xml" })
public class ACLTest extends DataManagementSupport {
	
	private static Logger logger = LoggerFactory.getLogger(ACLTest.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TaxonService taxonService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private AnnotationService annotationService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private OrganisationService sourceService;

    private UsernamePasswordAuthenticationToken token;

    private Group group;

    private Organisation source;

    private User user;

    /**
     * @throws java.lang.Exception
     *             if there is a problem
     */
    @Before
    public final void setUp() throws Exception {
        setUpTestData();

        for (Object obj : getSetUp()) {
            if (obj.getClass().equals(Taxon.class)) {
                taxonService.saveOrUpdate((Taxon) obj);
            } else if (obj.getClass().equals(Image.class)) {
                imageService.saveOrUpdate((Image) obj);
            } else if (obj.getClass().equals(Annotation.class)) {
                annotationService.saveOrUpdate((Annotation) obj);
            } else if (obj.getClass().equals(Organisation.class)) {
                sourceService.saveOrUpdate((Organisation) obj);
            } else if (obj.getClass().equals(User.class)) {
                userService.createUser((User) obj);
            } else if (obj.getClass().equals(Group.class)) {
                groupService.saveOrUpdate((Group) obj);
            }
        }
        token = new UsernamePasswordAuthenticationToken("admin@e-monocot.org",
                "sPePhAz6");
        Authentication authentication = authenticationManager
                .authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        userService.addPermission(source, "test", BasePermission.READ,
                Organisation.class);
        SecurityContextHolder.clearContext();
    }

    /**
     * @throws java.lang.Exception
     *             if there is a problem
     */
    @After
    public final void tearDown() throws Exception {
        setSetUp(new ArrayList<Object>());
        token = new UsernamePasswordAuthenticationToken("admin@e-monocot.org",
        "sPePhAz6");
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        userService.deletePermission(source, "test", BasePermission.READ,
                Organisation.class);
        
        while (!getTearDown().isEmpty()) {
            Object obj = getTearDown().pop();
            if (obj.getClass().equals(Taxon.class)) {
                taxonService.delete(((Taxon) obj).getIdentifier());
            } else if (obj.getClass().equals(Image.class)) {
                imageService.delete(((Image) obj).getIdentifier());
            } else if (obj.getClass().equals(Annotation.class)) {
                annotationService.delete(((Annotation) obj).getIdentifier());
            } else if (obj.getClass().equals(Organisation.class)) {
                sourceService.delete(((Organisation) obj).getIdentifier());
            } else if (obj.getClass().equals(User.class)) {
                userService.deleteUser(((User) obj).getIdentifier());
            } else if (obj.getClass().equals(Group.class)) {
                userService.deleteGroup(((Group) obj).getIdentifier());
            }
        }
        SecurityContextHolder.clearContext();
    }

    /**
     *
     */
    @Override
    public final void setUpTestData() {
        source = createSource("test", "http://example.com", "Test Organisation", "test@example.com");
        group = createGroup("test");
        user = createUser("authorized.user@e-monocot.org", "good.password", "authorizedUser");
        user.getGroups().add(group);
        User unauthorizedUser = createUser("unauthorized.user@e-monocot.org",
                "bad.password", "unauthorizedUser");
    }

    /**
     *
     */
    @Test
    public final void testACLWithoutPermission() {
        token = new UsernamePasswordAuthenticationToken(
                "unauthorized.user@e-monocot.org", "bad.password");
        Authentication authentication = authenticationManager
                .authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean adeExceptionThrown = false;

        try {
            sourceService.load("test");
        } catch (AccessDeniedException expected) {
            adeExceptionThrown = true;
        }

        assertTrue("An Access Denied Exception was expected",
                adeExceptionThrown);
        SecurityContextHolder.clearContext();
    }

    /**
     *
     */
    @Test
    public final void testACLWithPermission() {
        token = new UsernamePasswordAuthenticationToken(
                "authorized.user@e-monocot.org", "good.password");
        Authentication authentication = authenticationManager
                .authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        sourceService.load("test");
        SecurityContextHolder.clearContext();
    }

    /**
    *
    */
   @Test
   public final void testACLWithAdministratePermission() {
       token = new UsernamePasswordAuthenticationToken(
               "admin@e-monocot.org", "sPePhAz6");
       Authentication authentication = authenticationManager
               .authenticate(token);
       SecurityContextHolder.getContext().setAuthentication(authentication);
       sourceService.load("test");
       SecurityContextHolder.clearContext();
   }

  /**
   *
   */
  @Test
  public final void testListAces() {

      for (Object[] row : userService.listAces("test")) {
          logger.debug("Object: " + row[0] + " ACE: " + row[1]);
      }
  }
}
