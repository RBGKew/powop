package org.emonocot.checklist.view.oai.dc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.ChangeEventImpl;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.IdentifiableEntity;
import org.emonocot.checklist.test.DummyIdentifiableEntity;
import org.emonocot.checklist.view.oai.AbstractOaiPmhResponseView;
import org.emonocot.checklist.view.oai.AbstractOaiPmhViewTestCase;
import org.emonocot.model.pager.DefaultPageImpl;
import org.emonocot.model.pager.Page;
import org.joda.time.DateTime;
import org.openarchives.pmh.MetadataPrefix;
import org.openarchives.pmh.Request;
import org.openarchives.pmh.ResumptionToken;
import org.openarchives.pmh.Verb;

/**
 *
 * @author ben
 *
 */
public class LastPageListRecordsViewTest extends AbstractOaiPmhViewTestCase {

    /**
     *
     */
    private static final int NUMBER_OF_ENTITIES = 10;

    /**
    *
    */
   private static final int TOTAL_NUMBER_OF_ENTITIES = 100;

   /**
    *
    */
   private static final DateTime DATE_TIME
       = new DateTime(2005, 04, 25, 12, 0, 0, 0);

    @Override
    public final void doSetup(final Map model) {
        Request request = new Request();
        request.setVerb(Verb.ListRecords);
        request.setValue("http://www.example.com/oai");
        model.put("request", request);

        List<ChangeEvent<IdentifiableEntity>> list
            = new ArrayList<ChangeEvent<IdentifiableEntity>>();

        for (int i = 0; i < LastPageListRecordsViewTest.NUMBER_OF_ENTITIES; i++) {
            DummyIdentifiableEntity entity = new DummyIdentifiableEntity();
            entity.setTitle("title" + i);
            entity.setCreated(new DateTime());
            entity.setIdentifier("identifier" + i);
            list.add(new ChangeEventImpl<IdentifiableEntity>(entity,
                    ChangeType.CREATE, LastPageListRecordsViewTest.DATE_TIME));
        }
        Page<ChangeEvent<IdentifiableEntity>> results
            = new DefaultPageImpl<ChangeEvent<IdentifiableEntity>>(
                    LastPageListRecordsViewTest.TOTAL_NUMBER_OF_ENTITIES, 9,
                    LastPageListRecordsViewTest.NUMBER_OF_ENTITIES, list);
        model.put("object", results);
    }

    @Override
    public String getExpected() {
        return "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><?xml-stylesheet type='text/xsl' href='oai2.xsl'?><oai:OAI-PMH xmlns:oai='http://www.openarchives.org/OAI/2.0/' xmlns:oai__dc='http://www.openarchives.org/OAI/2.0/oai_dc/' xmlns:dc='http://purl.org/dc/elements/1.1/'><oai:responseDate>2011-05-13T15:44:00Z</oai:responseDate><oai:request verb='ListRecords'></oai:request><oai:ListRecords><oai:record><oai:header><oai:identifier>identifier0</oai:identifier><oai:datestamp>2005-04-25T11:00:00Z</oai:datestamp></oai:header><oai:metadata><oai_dc:dc xmlns:oai_dc='http://www.openarchives.org/OAI/2.0/oai_dc/'><dc:title xmlns:dc='http://purl.org/dc/elements/1.1/'>title0</dc:title><dc:date>2011-05-13T15:44:00Z</dc:date><dc:identifier>identifier0</dc:identifier></oai_dc:dc></oai:metadata></oai:record><oai:record><oai:header><oai:identifier>identifier1</oai:identifier><oai:datestamp>2005-04-25T11:00:00Z</oai:datestamp></oai:header><oai:metadata><oai_dc:dc xmlns:oai_dc='http://www.openarchives.org/OAI/2.0/oai_dc/'><dc:title xmlns:dc='http://purl.org/dc/elements/1.1/'>title1</dc:title><dc:date>2011-05-13T15:44:00Z</dc:date><dc:identifier>identifier1</dc:identifier></oai_dc:dc></oai:metadata></oai:record><oai:record><oai:header><oai:identifier>identifier2</oai:identifier><oai:datestamp>2005-04-25T11:00:00Z</oai:datestamp></oai:header><oai:metadata><oai_dc:dc xmlns:oai_dc='http://www.openarchives.org/OAI/2.0/oai_dc/'><dc:title xmlns:dc='http://purl.org/dc/elements/1.1/'>title2</dc:title><dc:date>2011-05-13T15:44:00Z</dc:date><dc:identifier>identifier2</dc:identifier></oai_dc:dc></oai:metadata></oai:record><oai:record><oai:header><oai:identifier>identifier3</oai:identifier><oai:datestamp>2005-04-25T11:00:00Z</oai:datestamp></oai:header><oai:metadata><oai_dc:dc xmlns:oai_dc='http://www.openarchives.org/OAI/2.0/oai_dc/'><dc:title xmlns:dc='http://purl.org/dc/elements/1.1/'>title3</dc:title><dc:date>2011-05-13T15:44:00Z</dc:date><dc:identifier>identifier3</dc:identifier></oai_dc:dc></oai:metadata></oai:record><oai:record><oai:header><oai:identifier>identifier4</oai:identifier><oai:datestamp>2005-04-25T11:00:00Z</oai:datestamp></oai:header><oai:metadata><oai_dc:dc xmlns:oai_dc='http://www.openarchives.org/OAI/2.0/oai_dc/'><dc:title xmlns:dc='http://purl.org/dc/elements/1.1/'>title4</dc:title><dc:date>2011-05-13T15:44:00Z</dc:date><dc:identifier>identifier4</dc:identifier></oai_dc:dc></oai:metadata></oai:record><oai:record><oai:header><oai:identifier>identifier5</oai:identifier><oai:datestamp>2005-04-25T11:00:00Z</oai:datestamp></oai:header><oai:metadata><oai_dc:dc xmlns:oai_dc='http://www.openarchives.org/OAI/2.0/oai_dc/'><dc:title xmlns:dc='http://purl.org/dc/elements/1.1/'>title5</dc:title><dc:date>2011-05-13T15:44:00Z</dc:date><dc:identifier>identifier5</dc:identifier></oai_dc:dc></oai:metadata></oai:record><oai:record><oai:header><oai:identifier>identifier6</oai:identifier><oai:datestamp>2005-04-25T11:00:00Z</oai:datestamp></oai:header><oai:metadata><oai_dc:dc xmlns:oai_dc='http://www.openarchives.org/OAI/2.0/oai_dc/'><dc:title xmlns:dc='http://purl.org/dc/elements/1.1/'>title6</dc:title><dc:date>2011-05-13T15:44:00Z</dc:date><dc:identifier>identifier6</dc:identifier></oai_dc:dc></oai:metadata></oai:record><oai:record><oai:header><oai:identifier>identifier7</oai:identifier><oai:datestamp>2005-04-25T11:00:00Z</oai:datestamp></oai:header><oai:metadata><oai_dc:dc xmlns:oai_dc='http://www.openarchives.org/OAI/2.0/oai_dc/'><dc:title xmlns:dc='http://purl.org/dc/elements/1.1/'>title7</dc:title><dc:date>2011-05-13T15:44:00Z</dc:date><dc:identifier>identifier7</dc:identifier></oai_dc:dc></oai:metadata></oai:record><oai:record><oai:header><oai:identifier>identifier8</oai:identifier><oai:datestamp>2005-04-25T11:00:00Z</oai:datestamp></oai:header><oai:metadata><oai_dc:dc xmlns:oai_dc='http://www.openarchives.org/OAI/2.0/oai_dc/'><dc:title xmlns:dc='http://purl.org/dc/elements/1.1/'>title8</dc:title><dc:date>2011-05-13T15:44:00Z</dc:date><dc:identifier>identifier8</dc:identifier></oai_dc:dc></oai:metadata></oai:record><oai:record><oai:header><oai:identifier>identifier9</oai:identifier><oai:datestamp>2005-04-25T11:00:00Z</oai:datestamp></oai:header><oai:metadata><oai_dc:dc xmlns:oai_dc='http://www.openarchives.org/OAI/2.0/oai_dc/'><dc:title xmlns:dc='http://purl.org/dc/elements/1.1/'>title9</dc:title><dc:date>2011-05-13T15:44:00Z</dc:date><dc:identifier>identifier9</dc:identifier></oai_dc:dc></oai:metadata></oai:record></oai:ListRecords></oai:OAI-PMH>";
    }

    @Override
    public AbstractOaiPmhResponseView setView() {
        return new ListRecordsView();
    }

}
