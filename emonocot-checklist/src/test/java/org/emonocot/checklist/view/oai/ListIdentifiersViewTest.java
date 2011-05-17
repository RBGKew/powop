package org.emonocot.checklist.view.oai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.ChangeEventImpl;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.IdentifiableEntity;
import org.emonocot.checklist.persistence.pager.DefaultPageImpl;
import org.emonocot.checklist.persistence.pager.Page;
import org.emonocot.checklist.test.DummyIdentifiableEntity;
import org.openarchives.pmh.MetadataPrefix;
import org.openarchives.pmh.Request;
import org.openarchives.pmh.ResumptionToken;
import org.openarchives.pmh.Verb;

/**
 *
 * @author ben
 *
 */
public class ListIdentifiersViewTest extends AbstractOaiPmhViewTestCase {

   /**
    *
    */
   private static final DateTime DATE_TIME
       = new DateTime(2004, 12, 25, 12, 0, 0, 0);

   /**
    *
    */
   private static final int NUMBER_OF_ENTITIES = 10;

  /**
   *
   */
  private static final int TOTAL_NUMBER_OF_ENTITIES = 100;

    @Override
    public final void doSetup(final Map model) {
        Request request = new Request();
        request.setVerb(Verb.ListIdentifiers);
        request.setValue("http://www.example.com/oai");
        model.put("request", request);

        List<ChangeEvent<IdentifiableEntity>> list
            = new ArrayList<ChangeEvent<IdentifiableEntity>>();

        for (int i = 0; i < ListIdentifiersViewTest.NUMBER_OF_ENTITIES; i++) {
            IdentifiableEntity entity = new DummyIdentifiableEntity();
            entity.setIdentifier("identifier" + i);
            list.add(new ChangeEventImpl<IdentifiableEntity>(entity,
                    ChangeType.CREATE, ListIdentifiersViewTest.DATE_TIME));
        }
        Page<ChangeEvent<IdentifiableEntity>> results
            = new DefaultPageImpl<ChangeEvent<IdentifiableEntity>>(
                ListIdentifiersViewTest.TOTAL_NUMBER_OF_ENTITIES, 0,
                ListIdentifiersViewTest.NUMBER_OF_ENTITIES, list);
        model.put("object", results);

        model.put("resumptionToken", new ResumptionToken(
                ListIdentifiersViewTest.TOTAL_NUMBER_OF_ENTITIES,
                ListIdentifiersViewTest.NUMBER_OF_ENTITIES,
                0,
                ListIdentifiersViewTest.DATE_TIME,
                ListIdentifiersViewTest.DATE_TIME,
                MetadataPrefix.OAI_DC, null));

    }

    @Override
    public final String getExpected() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><oai:OAI-PMH xmlns:oai=\"http://www.openarchives.org/OAI/2.0/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:owl=\"http://www.w3.org/2002/07/owl#\" xmlns:dcterms=\"http://purl.org/dc/terms/\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:tcom=\"http://rs.tdwg.org/ontology/voc/Common#\" xmlns:tpc=\"http://rs.tdwg.org/ontology/voc/PublicationCitation#\" xmlns:tc=\"http://rs.tdwg.org/ontology/voc/TaxonConcept#\" xmlns:tp=\"http://rs.tdwg.org/ontology/voc/Person#\" xmlns:tt=\"http://rs.tdwg.org/ontology/voc/Team#\" xmlns:tn=\"http://rs.tdwg.org/ontology/voc/TaxonName#\" xmlns:spm=\"http://rs.tdwg.org/ontology/voc/SpeciesProfileModel#\" xmlns:gr=\"http://rs.tdwg.org/ontology/voc/GeographicRegion#\" xmlns:spmi=\"http://rs.tdwg.org/ontology/voc/SPMInfoItems#\"><oai:responseDate>2010-06-18T11:40:31.091+01:00</oai:responseDate><oai:request verb=\"ListIdentifiers\"></oai:request><oai:ListIdentifiers><oai:header><oai:identifier>identifier0</oai:identifier><oai:datestamp>2004-12-25T12:00:00Z</oai:datestamp></oai:header><oai:header><oai:identifier>identifier1</oai:identifier><oai:datestamp>2004-12-25T12:00:00Z</oai:datestamp></oai:header><oai:header><oai:identifier>identifier2</oai:identifier><oai:datestamp>2004-12-25T12:00:00Z</oai:datestamp></oai:header><oai:header><oai:identifier>identifier3</oai:identifier><oai:datestamp>2004-12-25T12:00:00Z</oai:datestamp></oai:header><oai:header><oai:identifier>identifier4</oai:identifier><oai:datestamp>2004-12-25T12:00:00Z</oai:datestamp></oai:header><oai:header><oai:identifier>identifier5</oai:identifier><oai:datestamp>2004-12-25T12:00:00Z</oai:datestamp></oai:header><oai:header><oai:identifier>identifier6</oai:identifier><oai:datestamp>2004-12-25T12:00:00Z</oai:datestamp></oai:header><oai:header><oai:identifier>identifier7</oai:identifier><oai:datestamp>2004-12-25T12:00:00Z</oai:datestamp></oai:header><oai:header><oai:identifier>identifier8</oai:identifier><oai:datestamp>2004-12-25T12:00:00Z</oai:datestamp></oai:header><oai:header><oai:identifier>identifier9</oai:identifier><oai:datestamp>2004-12-25T12:00:00Z</oai:datestamp></oai:header><oai:resumptionToken completeListSize=\"100\" cursor=\"0\">f86585c5-9522-40d7-a973-52832023883a</oai:resumptionToken></oai:ListIdentifiers></oai:OAI-PMH>";
    }

    @Override
    public final AbstractOaiPmhResponseView setView() {
        return new ListIdentifiersView();
    }

}
