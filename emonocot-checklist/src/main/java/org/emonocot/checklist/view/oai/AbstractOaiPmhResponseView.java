package org.emonocot.checklist.view.oai;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamResult;

import org.dozer.Mapper;
import org.emonocot.checklist.controller.oai.AbstractOaiPmhController;
import org.openarchives.pmh.OAIPMH;
import org.openarchives.pmh.Request;
import org.openarchives.pmh.marshall.XStreamMarshaller;
import org.springframework.oxm.Marshaller;
import org.springframework.web.servlet.view.AbstractView;

/**
 * View class which takes a MetadataResponse and returns the Source for
 * serialization.
 *
 * @author ben
 * @see javax.xml.transform.Source
 * @see com.ibm.lsid.MetadataResponse
 */
public abstract class AbstractOaiPmhResponseView extends AbstractView {

    /**
     *
     */
    private Marshaller marshaller;

    /**
     *
     */
    private Mapper mapper;

    /**
     * Set any additional headers e.g. stylesheets
     */
    private String xmlHeaders
      = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>"
       + "<?xml-stylesheet type='text/xsl' href='oai2.xsl'?>";

    /**
     *
     * @param newXmlHeaders Set the xml headers
     */
    public final void setXmlHeaders(final String newXmlHeaders) {
        this.xmlHeaders = newXmlHeaders;
    }

    /**
     *
     * @param newMarshaller Set the marshaller to use
     */
    public final void setMarshaller(final Marshaller newMarshaller) {
        this.marshaller = newMarshaller;
    }

    /**
     *
     * @param newMapper Set the mapper to use
     */
    public final void setMapper(final Mapper newMapper) {
        this.mapper = newMapper;
    }

    /**
     *
     * @param oaiPmh The oai pmh object to serialize
     * @param model The model to merge into the response
     */
    protected abstract void constructResponse(OAIPMH oaiPmh,
            Map<String, Object> model);

    @Override
    public final void renderMergedOutputModel(final Map<String, Object> model,
            final HttpServletRequest request,
            final HttpServletResponse response)
            throws Exception {
        response.setContentType(getContentType());
        OAIPMH oaiPmh = new OAIPMH();
        oaiPmh.setRequest((Request) model
                .get(AbstractOaiPmhController.REQUEST_KEY));
        oaiPmh.getRequest().setValue(request.getRequestURI());
        constructResponse(oaiPmh, model);
        OutputStream outputStream = response.getOutputStream();

        /**
         * XStream does not write out the xml header by default
         */
        if (marshaller.getClass().equals(XStreamMarshaller.class)) {
           Writer writer = new OutputStreamWriter(outputStream);
           if (xmlHeaders != null) {
               writer.write(xmlHeaders);
           }
           writer.flush();
        }
        marshaller
                .marshal(oaiPmh, new StreamResult(outputStream));
    }

    /**
     *
     * @return the mapper
     */
    public final Mapper getMapper() {
        return mapper;
    }

}
