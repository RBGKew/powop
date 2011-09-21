package org.emonocot.model.marshall.xml;

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

    /**
     *
     */
    private static boolean libraryPresent;

    /**
     *
     */
    private QNameMap qnameMap;

    /**
     *
     */
    private XMLInputFactory inputFactory;

    /**
     *
     */
    private XMLOutputFactory outputFactory;

    /**
     *
     * @param newXmlInputFactory Set the xml input factory
     */
    public final void setXmlInputFactory(
            final XMLInputFactory newXmlInputFactory) {
        this.inputFactory = newXmlInputFactory;
    }

    /**
     *
     * @param newXmlOutputFactory Set the xml output factory
     */
    public final void setXmlOutputFactory(
            final XMLOutputFactory newXmlOutputFactory) {
        this.outputFactory = newXmlOutputFactory;
    }

    /**
     * write the xml declaration or not?
     */
    private boolean startDocument = false;

    /**
     *
     */
    public StaxDriver() {
        this.qnameMap = new QNameMap();
    }

    /**
     *
     * @param newQnameMap Set the QName Map
     */
    public StaxDriver(final QNameMap newQnameMap) {
        this(newQnameMap, false);
    }

    /**
     * @deprecated since 1.2, use an explicit call to
     *             {@link #setRepairingNamespace(boolean)}
     * @param newQnameMap
     *            Set the QName map
     * @param newRepairingNamespace
     *            Should the driver try to repair the namespace
     */
    public StaxDriver(final QNameMap newQnameMap,
            final boolean newRepairingNamespace) {
        this(newQnameMap, new XmlFriendlyReplacer());
        setRepairingNamespace(newRepairingNamespace);
    }

    /**
     * @since 1.2
     * @param newQNameMap Set the QName Map
     * @param replacer Set the Xml Replacer
     */
    public StaxDriver(final QNameMap newQNameMap,
            final XmlFriendlyReplacer replacer) {
        super(replacer);
        this.qnameMap = newQNameMap;
    }

    /**
     * @since 1.2
     * @param replacer Set the Xml Replacer
     */
    public StaxDriver(final XmlFriendlyReplacer replacer) {
        this(new QNameMap(), replacer);
    }

    /**
     *
     * @param xml Set the java.io.Reader
     * @return HierarchicalStreamReader a hierarchical stream reader
     */
    public final HierarchicalStreamReader createReader(final Reader xml) {
        loadLibrary();
        try {
            return createStaxReader(createParser(xml));
        } catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    /**
     *
     * @param in Set the input stream
     * @return HierarchicalStreamReader a hierarchical stream reader
     */
    public final HierarchicalStreamReader createReader(final InputStream in) {
        loadLibrary();
        try {
            return createStaxReader(createParser(in));
        } catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    /**
     *
     */
    private void loadLibrary() {
        if (!libraryPresent) {
            try {
                Class.forName("javax.xml.stream.XMLStreamReader");
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(
                        "StAX API is not present. Specify another driver."
                      + " For example: new XStream(new DomDriver())");
            }
            libraryPresent = true;
        }
    }

    /**
     *
     * @param out Set the writer
     * @return HierarchicalStreamWriter a hierarchical stream writer
     */
    public final HierarchicalStreamWriter createWriter(final Writer out) {
        try {
            return createStaxWriter(
                    getOutputFactory().createXMLStreamWriter(out),
                    startDocument);
        } catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    /**
     *
     * @param out Set the output stream
     * @return HierarchicalStreamWriter a hierarchical stream writer
     */
    public final HierarchicalStreamWriter createWriter(final OutputStream out) {
        try {
            return createStaxWriter(
                    getOutputFactory().createXMLStreamWriter(out),
                    startDocument);
        } catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    /**
     *
     * @param in Set the XML Stream reader
     * @return AbstractPullReader an abstract pull reader
     */
    public final AbstractPullReader createStaxReader(final XMLStreamReader in) {
        return new StaxReader(qnameMap, in, xmlFriendlyReplacer());
    }

    /**
     *
     * @param out
     *            Set the XML stream writer
     * @param writeStartEndDocument
     *            Write the start of the document?
     * @return a STaX writer
     * @throws XMLStreamException
     *             if there is a problem writing xml to the stream
     */
    public final StaxWriter createStaxWriter(final XMLStreamWriter out,
            final boolean writeStartEndDocument) throws XMLStreamException {
        return new StaxWriter(qnameMap, out, writeStartEndDocument,
                isRepairingNamespace(), xmlFriendlyReplacer());
    }

    /**
    *
    * @param out
    *            Set the XML stream writer
    * @return a STaX writer
    * @throws XMLStreamException
    *             if there is a problem writing xml to the stream
    */
    public final StaxWriter createStaxWriter(final XMLStreamWriter out)
            throws XMLStreamException {
        return createStaxWriter(out, true);
    }


    // Properties
    //-------------------------------------------------------------------------
    /**
     *
     * @return a QName map
     */
    public final QNameMap getQnameMap() {
        return qnameMap;
    }

    /**
     *
     * @param newQNameMap Set the QName map
     */
    public final void setQnameMap(final QNameMap newQNameMap) {
        this.qnameMap = newQNameMap;
    }

    /**
     *
     * @return an XML Input Factory
     */
    public final XMLInputFactory getInputFactory() {
        if (inputFactory == null) {
            inputFactory = XMLInputFactory.newInstance();
        }
        return inputFactory;
    }

    /**
     *
     * @return an XML Output Factory
     */
    public final XMLOutputFactory getOutputFactory() {
        if (outputFactory == null) {
            outputFactory = XMLOutputFactory.newInstance();
        }
        return outputFactory;
    }

    /**
     *
     * @return Is the STaX driver attempting to repair the namespaces?
     */
    public final boolean isRepairingNamespace() {
        return Boolean.TRUE.equals(getOutputFactory().getProperty(
                XMLOutputFactory.IS_REPAIRING_NAMESPACES));
    }

    /**
     * @since 1.2
     * @param repairing Attempt to repair the namespaces in the document
     */
    public final void setRepairingNamespace(final boolean repairing) {
        if (repairing) {
            getOutputFactory().setProperty(
                    XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.TRUE);
        } else {
            getOutputFactory().setProperty(
                    XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.FALSE);
        }
    }


    // Implementation methods
    //-------------------------------------------------------------------------
    /**
     *
     * @param xml Set the xml reader
     * @return XMLStreamReader an XML Stream Reader
     * @throws XMLStreamException if there is a problem reading the xml
     */
    protected final XMLStreamReader createParser(final Reader xml)
            throws XMLStreamException {
        return getInputFactory().createXMLStreamReader(xml);
    }

   /**
    *
    * @param xml Set the xml input stream
    * @return XMLStreamReader an XML Stream Reader
    * @throws XMLStreamException if there is a problem reading the xml
    */
    protected final XMLStreamReader createParser(final InputStream xml)
            throws XMLStreamException {
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
