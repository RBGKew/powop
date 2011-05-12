package org.emonocot.checklist.view.oai;

import java.util.Map;

import org.joda.time.DateTime;
import org.openarchives.pmh.DeletedRecord;
import org.openarchives.pmh.Granularity;
import org.openarchives.pmh.Identify;
import org.openarchives.pmh.Request;
import org.openarchives.pmh.Verb;

/**
 * Since jdk1.5.0 has an early version of xalan installed, and this
 * version doesn't handle namespaces properly, we have to ignore this
 * test since there is no way to override the parser, without replacing
 * this jar system-wide.
 *
 * @author bc00kg
 *
 */
public class IdentifyViewTest extends AbstractOaiPmhViewTestCase {

    /**
     *
     */
    private static final DateTime DATE_TIME
        = new DateTime(2004, 12, 25, 12, 0, 0, 0);

    /**
     * @param model the model
     */
    public final void doSetup(final Map model) {
        Identify identify = new Identify();
        identify.setRepositoryName("Repository Name");
        identify.setBaseURL("http://www.example.com/oai");
        identify.setProtocolVersion("2.0");
        identify.setEarliestDatestamp(IdentifyViewTest.DATE_TIME);
        identify.setDeletedRecord(DeletedRecord.NO);
        identify.setGranularity(Granularity.YYYY_MM_DD);
        identify.getAdminEmail().add("admin@example.com");
        model.put("object", identify);

        Request request = new Request();
        request.setVerb(Verb.Identify);
        request.setValue("http://www.example.com/oai");
        model.put("request", request);
    }

    @Override
    public final String getExpected() {
        return "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><?xml-stylesheet type='text/xsl' href='oai2.xsl'?><OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\"><responseDate>2010-06-18T11:36:44.411+01:00</responseDate><request verb=\"Identify\"></request><Identify><repositoryName>Repository Name</repositoryName><baseURL>http://www.example.com/oai</baseURL><protocolVersion>2.0</protocolVersion><adminEmail>admin@example.com</adminEmail><earliestDatestamp>2004-12-25T12:00:00Z</earliestDatestamp><deletedRecord>no</deletedRecord><granularity>YYYY-MM-DD</granularity></Identify></OAI-PMH>";
    }

    @Override
    public final AbstractOaiPmhResponseView setView() {
        return new IdentifyView();
    }
}
