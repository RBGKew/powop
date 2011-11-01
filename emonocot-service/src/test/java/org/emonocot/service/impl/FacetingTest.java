package org.emonocot.service.impl;

import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.hamcrest.collection.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.api.AnnotationService;
import org.emonocot.api.FacetName;
import org.emonocot.api.ImageService;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.Sorting;
import org.emonocot.api.SourceService;
import org.emonocot.api.TaxonService;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.media.Image;
import org.emonocot.model.pager.Page;
import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.emonocot.persistence.DataManagementSupport;
import org.hibernate.search.query.facet.Facet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/applicationContext-test.xml" })
public class FacetingTest extends DataManagementSupport {

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
   private SourceService sourceService;

    /**
     *
     */
    @Autowired
    private SearchableObjectService searchableObjectService;

    /**
     * @throws java.lang.Exception if there is a problem
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
            }
        }
    }

    /**
     * @throws java.lang.Exception if there is a problem
     */
    @After
    public final void tearDown() throws Exception {
        setSetUp(new ArrayList<Object>());
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
            }
        }
    }

    /**
     *
     */
    @Override
    public final void setUpTestData() {
        Source source = createSource("test", "http://example.com");
        Taxon taxon1 = createTaxon("Aus", "1", null, null, "Ausaceae", null,
                null, "(1753)", Rank.GENUS, TaxonomicStatus.accepted,
                source, new GeographicalRegion[] {});
        Taxon taxon2 = createTaxon("Aus bus", "2", taxon1, null, "Ausaceae",
                null, null, "(1775)", Rank.SPECIES, TaxonomicStatus.accepted,
                source, new GeographicalRegion[] {Continent.AUSTRALASIA,
                        Region.BRAZIL, Region.CARIBBEAN });
        Taxon taxon3 = createTaxon("Aus ceus", "3", taxon1, null, "Ausaceae",
                null, null, "(1805)", Rank.SPECIES, TaxonomicStatus.accepted,
                source, new GeographicalRegion[] {Region.NEW_ZEALAND });
        Taxon taxon4 = createTaxon("Aus deus", "4", null, taxon2, "Ausaceae",
                null, null, "(1895)", Rank.SPECIES, TaxonomicStatus.synonym,
                source, new GeographicalRegion[] {});
        Taxon taxon5 = createTaxon("Aus eus", "5", null, taxon3, "Ausaceae",
                null, null, "(1935)", Rank.SPECIES, TaxonomicStatus.synonym,
                source, new GeographicalRegion[] {});
        Image img1 = createImage("Aus", "1", source);
        Image img2 = createImage("Aus bus", "2", source);

    }

    /**
     *
     */
    @Test
    public final void testSearch() {
        Page<SearchableObject> pager = searchableObjectService.search("Aus", null,
                null, null, null, null, null);
        assertEquals("there should be seven objects saved", (Integer) 7,
                pager.getSize());
    }

    /**
    *
    */
    @Test
    public final void testSearchWithFacets() {
        Map<FacetName, Integer> selectedFacets = new HashMap<FacetName, Integer>();
        selectedFacets.put(FacetName.CLASS, 1);
        Page<SearchableObject> pager = searchableObjectService.search("Aus", null,
                null, null,
                new FacetName[] {FacetName.CLASS, FacetName.FAMILY,FacetName.CONTINENT, FacetName.AUTHORITY},
                selectedFacets, null);
        assertThat("There should be two facets returned",
                pager.getFacetNames(), hasItems("CLASS", "FAMILY"));

        List<Facet> classFacets = pager.getFacets().get("CLASS");
        String[] facetNames = new String[classFacets.size()];
        for (int i = 0; i < facetNames.length; i++) {
            facetNames[i] = classFacets.get(i).getValue();
        }

        assertThat("org.emonocot.model.taxon.Taxon should be a facet in CLASS",
                facetNames, hasItemInArray("org.emonocot.model.taxon.Taxon"));
        assertEquals("There should be one value for the FAMILY facet", 1, pager
                .getFacets().get("FAMILY").size());
    }

    /**
     * @throws Exception
     *             if there is a problem tearing down and adding the test data
     */
    @Test
    public final void testSearchWithFacetsInTaxonDao() throws Exception {
        Map<FacetName, Integer> selectedFacets = new HashMap<FacetName, Integer>();
        selectedFacets.put(FacetName.CLASS, 1);
        Page<Taxon> pager = taxonService.search("Aus", null, null, null,
                new FacetName[] {FacetName.CLASS, FacetName.FAMILY,
                        FacetName.CONTINENT, FacetName.AUTHORITY,
                        FacetName.RANK, FacetName.TAXONOMIC_STATUS },
                selectedFacets, null);
        assertEquals("There should be five taxa returned", (Integer)5, pager.getSize());
        assertThat("There should be two facets returned",
                pager.getFacetNames(), hasItems("CLASS", "FAMILY"));

        List<Facet> classFacets = pager.getFacets().get("CLASS");
        String[] facetNames = new String[classFacets.size()];
        for (int i = 0; i < facetNames.length; i++) {
            facetNames[i] = classFacets.get(i).getValue();
        }

        assertThat("org.emonocot.model.taxon.Taxon should be a facet in CLASS",
                facetNames, hasItemInArray("org.emonocot.model.taxon.Taxon"));
        assertThat("org.emonocot.model.media.Image should be a facet in CLASS",
                facetNames, hasItemInArray("org.emonocot.model.media.Image"));
        assertEquals("There should be one value for the FAMILY facet", 1, pager
                .getFacets().get("FAMILY").size());

        selectedFacets.put(FacetName.RANK, 1);
        pager = taxonService.search("Aus", null, null, null,
                new FacetName[] {FacetName.CLASS, FacetName.FAMILY,
                        FacetName.CONTINENT, FacetName.AUTHORITY,
                        FacetName.RANK, FacetName.TAXONOMIC_STATUS },
                selectedFacets, null);
        assertEquals("There should be four taxa returned", (Integer)4, pager.getSize());
    }

    /**
     *
     */
    @Test
    public final void testSearchWithSorting() {
        Page<SearchableObject> results = searchableObjectService.search("Au*",
                null, null, null, null, null, null);

        Sorting sort = new Sorting("label");
        results = searchableObjectService.search("Au*", null, null, null, null,
                null, sort);
        String[] actual = new String[results.getSize()];
        for (int i = 0; i < results.getSize(); i++) {
            if (results.getRecords().get(i).getClass().equals(Taxon.class)) {
                actual[i] = ((Taxon) results.getRecords().get(i)).getName();
            } else {
                actual[i] = ((Image) results.getRecords().get(i)).getCaption();
            }
        }

        String[] expected = new String[] {"Aus", "Aus", "Aus bus", "Aus bus",
                "Aus ceus", "Aus deus", "Aus eus" };

        assertArrayEquals(expected, actual);
    }
}
