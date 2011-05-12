package org.openarchives.pmh.marshall;

import java.io.Writer;

import javax.xml.stream.XMLStreamException;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.QNameMap;

/**
 * Stax driver which allows for configuration of the start document
 * parameter on the stax writer - meaning that a custom xml
 * declaration and, if desired, xml processing instructions can
 * be written to the stream in advance of the marshalling.
 *
 *
 * @author ben
 *
 */
public class StaxDriver extends com.thoughtworks.xstream.io.xml.StaxDriver {

    /**
     * write the xml declaration or not?
     */
    private boolean startDocument = false;

    /**
     *
     * @param qNameMap the qNameMap to configure the stax
     *                 writers with.
     */
    public StaxDriver(final QNameMap qNameMap) {
        super(qNameMap);
    }

    /**
     *
     * @param doStartDocument Set the start document parameter on the
     *                        stax writer
     */
    public final void setStartDocument(final boolean doStartDocument) {
        this.startDocument = doStartDocument;
    }

    @Override
    public final HierarchicalStreamWriter createWriter(final Writer out) {
        try {
            return createStaxWriter(getOutputFactory()
                    .createXMLStreamWriter(out), startDocument);
        } catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }


}
