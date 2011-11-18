package org.emonocot.portal.driver;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Properties;
import java.util.Stack;

import org.emonocot.api.GroupService;
import org.emonocot.api.ImageService;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.SourceService;
import org.emonocot.api.TaxonService;
import org.emonocot.api.UserService;
import org.emonocot.model.common.Base;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.GeographyConverter;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.emonocot.model.user.Group;
import org.emonocot.model.user.Permission;
import org.emonocot.model.user.User;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author ben
 *
 */
@Component
public class TestDataManager {

    /**
     *
     */
    private GeographyConverter geographyConverter = new GeographyConverter();

    /**
     *
     */
    private DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();

    /**
     *
     */
    private Stack<Base> data = new Stack<Base>();

    /**
     *
     */
    private String username;

    /**
     *
     */
    private String password;

    /**
     * @throws IOException if there is a problem loading the properties file
     *
     */
    public TestDataManager() throws IOException {
        Resource propertiesFile = new ClassPathResource(
                "application.properties");
        Properties properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        username = properties.getProperty("functional.test.username", null);
        password = properties.getProperty("functional.test.password", null);
    }

    /**
    *
    */
   @Autowired
   private ImageService imageService;

    /**
     *
     */
    @Autowired
    private TaxonService taxonService;

    /**
     *
     */
    @Autowired
    private ReferenceService referenceService;

    /**
    *
    */
   @Autowired
   private SourceService sourceService;

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
    private Authentication previousAuthentication = null;

    /**
    *
    * @param identifier Set the identifier
     * @param caption Set the caption
     * @param url Set the url
     * @param source Set the source
    */
    public final void createImage(final String identifier,
            final String caption, final String url, final String source) {
       enableAuthentication();
       Image image = new Image();
       image.setCaption(caption);
       image.setUrl(url);
       image.setIdentifier(identifier);
       if (source != null) {
           Source s = new Source();
           s.setIdentifier(source);
           image.setAuthority(s);
           image.getSources().add(s);
       }
       imageService.save(image);
       data.push(image);
       disableAuthentication();
    }

   /**
    * @param objectType Set the object type
    */
   public final void createAcl(final String objectType) {
       enableAuthentication();
//           Acl acl = new Acl();
//           source.setName(row.name);
//           sourceService.save(source);
//           data.push(source);
       disableAuthentication();
   }

    /**
     *
     * @param identifier
     *            Set the identifier
     * @param newPassword
     *            Set the password
     * @param group1 Set the first group
     */
    public final void createUser(final String identifier,
            final String newPassword, final String group1) {
        enableAuthentication();
        User user = new User();
        user.setIdentifier(identifier);
        user.setPassword(newPassword);
        if (group1 != null) {
            Group g1 = new Group();
            g1.setIdentifier(group1);
            user.getGroups().add(g1);
        }
        userService.save(user);
        data.push(user);
        disableAuthentication();
   }

   /**
    *
    * @param uri Set the uri
    * @param name Set the name
    */
   public final void createSourceSystem(final String identifier, String uri) {
        enableAuthentication();
        Source source = new Source();
        source.setIdentifier(identifier);
        source.setUri(uri);
        sourceService.save(source);
        data.push(source);
        disableAuthentication();
   }

   /**
    *
    */
   private void disableAuthentication() {
       SecurityContext securityContext = SecurityContextHolder.getContext();
       securityContext.setAuthentication(previousAuthentication);
    }

   /**
    *
    */
    private void enableAuthentication() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        previousAuthentication = securityContext.getAuthentication();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        securityContext
          .setAuthentication(
                  new TestAuthentication(user));

    }

    /**
     *
     * @param name
     *            Set the name
     */
   public final void assertNoTaxaWithName(final String name) {
       for (int i = 0; i < data.size(); i++) {
           Object o = data.get(i);
           if (o.getClass().equals(Taxon.class)) {
               Taxon t = (Taxon) o;
               if (name.equals(t.getName())) {
                   fail();
               }
           }
       }
   }

    /**
    *
    * @param name Set the name
     * @param family Set the family
     * @param identifier Set the identifier
     * @param rank Set the rank
     * @param status Set the status
     * @param diagnostic Set the diagnostic
     * @param habitat Set the habitat
     * @param general TODO
     * @param protologue Set the protologue
     * @param microReference TODO
     * @param image1 Set the image1
     * @param image2 Set the image2
     * @param image3 Set the image3
     * @param distribution1 Set the distribution1
     * @param distribution2 Set the distribution2
     * @param distribution3 Set the distribution3
     * @param source Set the source
     * @param created TODO
    *
    */
    public final void createTaxon(final String name, final String family,
            final String identifier, final String rank, final String status,
            final String diagnostic, final String habitat,
            final String general, final String protologue, String microReference,
            final String image1, final String image2,
            final String image3, final String distribution1, final String distribution2, final String distribution3, String source, String created) {
        enableAuthentication();
        Taxon taxon = new Taxon();
        data.push(taxon);
        taxon.setName(name);
        taxon.setFamily(family);
        taxon.setIdentifier(identifier);
        taxon.setProtologueMicroReference(microReference);
        if (rank != null && rank.length() > 0) {
            taxon.setRank(Rank.valueOf(rank));
        }
        if (status != null && status.length() > 0) {
            taxon.setStatus(TaxonomicStatus.valueOf(status));
        }
        if (diagnostic != null && diagnostic.length() > 0) {
            createTextualData(taxon, diagnostic, Feature.diagnostic);
        }
        if (habitat != null && habitat.length() > 0) {
            createTextualData(taxon, habitat, Feature.habitat);
        }
        if (general != null && general.length() > 0) {
            createTextualData(taxon, general, Feature.general);
        }
        if (protologue != null && protologue.length() > 0) {
            Reference reference = new Reference();
            reference.setIdentifier(protologue);
            taxon.setProtologue(reference);
        }
        if (image1 != null && image1.length() > 0) {
            Image image = new Image();
            image.setIdentifier(image1);
            taxon.getImages().add(0, image);
        }
        if (image2 != null && image2.length() > 0) {
            Image image = new Image();
            image.setIdentifier(image2);
            taxon.getImages().add(1, image);
        }
        if (image3 != null && image3.length() > 0) {
            Image image = new Image();
            image.setIdentifier(image3);
            taxon.getImages().add(2, image);
        }
        if (distribution1 != null && distribution1.length() > 0) {
            Distribution distribution = new Distribution();
            GeographicalRegion geographicalRegion = geographyConverter
                    .convert(distribution1);
            distribution.setRegion(geographicalRegion);
            distribution.setTaxon(taxon);
            taxon.getDistribution().put(geographicalRegion, distribution);
        }
        if (distribution2 != null && distribution2.length() > 0) {
            Distribution distribution = new Distribution();
            GeographicalRegion geographicalRegion = geographyConverter
                    .convert(distribution2);
            distribution.setRegion(geographicalRegion);
            distribution.setTaxon(taxon);
            taxon.getDistribution().put(geographicalRegion, distribution);
        }
        if (distribution3 != null && distribution3.length() > 0) {
            Distribution distribution = new Distribution();
            GeographicalRegion geographicalRegion = geographyConverter
                    .convert(distribution3);
            distribution.setRegion(geographicalRegion);
            distribution.setTaxon(taxon);
            taxon.getDistribution().put(geographicalRegion, distribution);
        }
        if (source != null && source.length() > 0) {
            Source s = new Source();
            s.setIdentifier(source);
            taxon.setAuthority(s);
            taxon.getSources().add(s);
        }
        if (created != null && created.length() > 0) {
            DateTime dateTime = dateTimeFormatter.parseDateTime(created);
            taxon.setCreated(dateTime);
        }
        taxonService.save(taxon);

        disableAuthentication();
   }

    /**
     *
     * @param base the object to register for deletion
     */
    public final void registerObject(final Base base) {
        data.push(base);
    }

  /**
   *
   * @param identifier Set the identifier
   * @param title Set the title
   * @param datePublished Set the datePublished
   * @param volume Set the volume
   * @param page Set the page
   */
    public final void createReference(final String identifier,
            final String title, final String datePublished,
            final String volume, final String page) {
        enableAuthentication();
        Reference r = new Reference();
        data.push(r);
        r.setIdentifier(identifier);
        r.setTitle(title);
        r.setDatePublished(datePublished);
        r.setVolume(volume);
        r.setPages(page);
        referenceService.save(r);
    }

  /**
   *
   * @param taxon Set the taxon
   * @param text Set the text
   * @param feature Set the feature
   */
  private void createTextualData(final Taxon taxon, final String text,
          final Feature feature) {
      TextContent textContent = new TextContent();
      textContent.setContent(text);
      textContent.setFeature(feature);
      textContent.setTaxon(taxon);
      taxon.getContent().put(feature, textContent);
  }

    /**
     *
     */
    public final void tearDown() {
        enableAuthentication();
        while (!data.isEmpty()) {
            Base base = data.pop();
            if (base instanceof Taxon) {
                taxonService.delete(base.getIdentifier());
            } else if (base instanceof Image) {
                imageService.delete(base.getIdentifier());
            } else if (base instanceof Reference) {
                referenceService.delete(base.getIdentifier());
            } else if (base instanceof User) {
                userService.delete(base.getIdentifier());
            } else if (base instanceof Group) {
                groupService.delete(base.getIdentifier());
            } else if (base instanceof Source) {
                sourceService.delete(base.getIdentifier());
            }
        }
        disableAuthentication();
    }

    /**
     *
     * @param name Set the name of the group
     * @param permission1 Set the first permission of the group
     */
    public final void createGroup(final String name, final String permission1) {
        enableAuthentication();
        Group group = new Group();
        data.push(group);
        group.setIdentifier(name);
        if (permission1 != null) {
            group.getPermissions().add(Permission.valueOf(permission1));
        }
        groupService.save(group);
        disableAuthentication();
    }
}
