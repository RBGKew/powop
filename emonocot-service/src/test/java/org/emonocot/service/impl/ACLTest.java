package org.emonocot.service.impl;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.emonocot.api.AnnotationService;
import org.emonocot.api.GroupService;
import org.emonocot.api.ImageService;
import org.emonocot.api.SourceService;
import org.emonocot.api.TaxonService;
import org.emonocot.api.UserService;
import org.emonocot.model.Annotation;
import org.emonocot.model.Image;
import org.emonocot.model.Source;
import org.emonocot.model.Taxon;
import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.User;
import org.emonocot.test.DataManagementSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    /**
     *
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
    *
    */
    @Autowired
    private TaxonService taxonService;

    /**
    *
    */
    @Autowired
    private ImageService imageService;

    /**
    *
    */
    @Autowired
    private AnnotationService annotationService;

    /**
     *
     */
    @Autowired
    private UserService userService;

    /**
     *
     */
    @Autowired
    private GroupService groupService;

    /**
    *
    */
    @Autowired
    private SourceService sourceService;

    /**
     *
     */
    private UsernamePasswordAuthenticationToken token;

    /**
     *
     */
    private Group group;

    /**
     *
     */
    private Source source;

    /**
     *
     */
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
            } else if (obj.getClass().equals(Source.class)) {
                sourceService.saveOrUpdate((Source) obj);
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
                Source.class);
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
                Source.class);
        SecurityContextHolder.clearContext();
        while (!getTearDown().isEmpty()) {
            Object obj = getTearDown().pop();
            if (obj.getClass().equals(Taxon.class)) {
                taxonService.delete(((Taxon) obj).getIdentifier());
            } else if (obj.getClass().equals(Image.class)) {
                imageService.delete(((Image) obj).getIdentifier());
            } else if (obj.getClass().equals(Annotation.class)) {
                annotationService.delete(((Annotation) obj).getIdentifier());
            } else if (obj.getClass().equals(Source.class)) {
                sourceService.delete(((Source) obj).getIdentifier());
            } else if (obj.getClass().equals(User.class)) {
                userService.deleteUser(((User) obj).getIdentifier());
            } else if (obj.getClass().equals(Group.class)) {
                userService.deleteGroup(((Group) obj).getIdentifier());
            }
        }
    }

    /**
     *
     */
    @Override
    public final void setUpTestData() {
        source = createSource("test", "http://example.com");
        group = createGroup("test");
        user = createUser("authorized.user@e-monocot.org", "good.password");
        user.getGroups().add(group);
        User unauthorizedUser = createUser("unauthorized.user@e-monocot.org",
                "bad.password");
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
          System.out.println("Object: " + row[0] + " ACE: " + row[1]);
      }
  }
}
