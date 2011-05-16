package org.emonocot.model.marshall;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.AbstractPullReader;
import com.thoughtworks.xstream.io.xml.AbstractXmlDriver;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxReader;
import com.thoughtworks.xstream.io.xml.StaxWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;

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
public class StaxDriver extends AbstractXmlDriver {

    private static boolean libraryPresent;

    private QNameMap qnameMap;
    private XMLInputFactory inputFactory;
    private XMLOutputFactory outputFactory;
    
    public void setXmlInputFactory(XMLInputFactory newXmlInputFactory) {
        this.inputFactory = newXmlInputFactory;
    }
    
    public void setXmlOutputFactory(XMLOutputFactory newXmlOutputFactory) {
        this.outputFactory = newXmlOutputFactory;
    }
    
    /**
     * write the xml declaration or not?
     */
    private boolean startDocument = false;

    public StaxDriver() {
        this.qnameMap = new QNameMap();
    }

    public StaxDriver(QNameMap qnameMap) {
        this(qnameMap, false);
    }

    /**
     * @deprecated since 1.2, use an explicit call to {@link #setRepairingNamespace(boolean)}
     */
    public StaxDriver(QNameMap qnameMap, boolean repairingNamespace) {
        this(qnameMap, new XmlFriendlyReplacer());
        setRepairingNamespace(repairingNamespace);
    }

    /**
     * @since 1.2
     */
    public StaxDriver(QNameMap qnameMap, XmlFriendlyReplacer replacer) {
        super(replacer);
        this.qnameMap = qnameMap;
    }
    
    /**
     * @since 1.2
     */
    public StaxDriver(XmlFriendlyReplacer replacer) {
        this(new QNameMap(), replacer);
    }

    public HierarchicalStreamReader createReader(Reader xml) {
        loadLibrary();
        try {
            return createStaxReader(createParser(xml));
        }
        catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamReader createReader(InputStream in) {
        loadLibrary();
        try {
            return createStaxReader(createParser(in));
        }
        catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    private void loadLibrary() {
        if (!libraryPresent) {
            try {
                Class.forName("javax.xml.stream.XMLStreamReader");
            }
            catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("StAX API is not present. Specify another driver." +
                        " For example: new XStream(new DomDriver())");
            }
            libraryPresent = true;
        }
    }

    public HierarchicalStreamWriter createWriter(Writer out) {
        try {
            return createStaxWriter(getOutputFactory().createXMLStreamWriter(out), startDocument);
        }
        catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamWriter createWriter(OutputStream out) {
        try {
            return createStaxWriter(getOutputFactory().createXMLStreamWriter(out), startDocument);
        }
        catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    public AbstractPullReader createStaxReader(XMLStreamReader in) {
        return new StaxReader(qnameMap, in, xmlFriendlyReplacer());
    }

    public StaxWriter createStaxWriter(XMLStreamWriter out, boolean writeStartEndDocument) throws XMLStreamException {
        return new StaxWriter(qnameMap, out, writeStartEndDocument, isRepairingNamespace(), xmlFriendlyReplacer());
    }

    public StaxWriter createStaxWriter(XMLStreamWriter out) throws XMLStreamException {
        return createStaxWriter(out, true);
    }


    // Properties
    //-------------------------------------------------------------------------
    public QNameMap getQnameMap() {
        return qnameMap;
    }

    public void setQnameMap(QNameMap qnameMap) {
        this.qnameMap = qnameMap;
    }

    public XMLInputFactory getInputFactory() {
        if (inputFactory == null) {
            inputFactory = XMLInputFactory.newInstance();
        }
        return inputFactory;
    }

    public XMLOutputFactory getOutputFactory() {
        if (outputFactory == null) {
            outputFactory = XMLOutputFactory.newInstance();
        }
        return outputFactory;
    }

    public boolean isRepairingNamespace() {
        return Boolean.TRUE.equals(getOutputFactory().getProperty(
                XMLOutputFactory.IS_REPAIRING_NAMESPACES));
    }

    /**
     * @since 1.2
     */
    public void setRepairingNamespace(boolean repairing) {
        getOutputFactory().setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES,
                repairing ? Boolean.TRUE : Boolean.FALSE);
    }


    // Implementation methods
    //-------------------------------------------------------------------------
    protected XMLStreamReader createParser(Reader xml) throws XMLStreamException {
        return getInputFactory().createXMLStreamReader(xml);
    }

    protected XMLStreamReader createParser(InputStream xml) throws XMLStreamException {
        return getInputFactory().createXMLStreamReader(xml);
    }

    /**
     *
     * @param doStartDocument Set the start document parameter on the
     *                        stax writer
     */
    public final void setStartDocument(final boolean doStartDocument) {
        this.startDocument = doStartDocument;
    }

}
