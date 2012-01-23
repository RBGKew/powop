/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.emonocot.job.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.WriteFailedException;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.batch.item.util.ExecutionContextUserSupport;
import org.springframework.batch.item.util.FileUtils;
import org.springframework.batch.item.xml.StaxWriterCallback;
import org.springframework.batch.item.xml.stax.NoStartEndDocumentStreamWriter;
import org.springframework.batch.support.transaction.TransactionAwareBufferedWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.oxm.Marshaller;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * An implementation of {@link ItemWriter} which uses StAX and
 * {@link Marshaller} for serializing object to XML.
 *
 * This item writer also provides restart, statistics and transaction features
 * by implementing corresponding interfaces.
 *
 * The implementation is *not* thread-safe.
 *
 * @author Peter Zozom
 * @author Robert Kasanicky
 * @param <T> the type of item written
 */
public class StaxEventItemWriter<T> extends ExecutionContextUserSupport
        implements ResourceAwareItemWriterItemStream<T>, InitializingBean {

    /**
     *
     */
    private static Log log = LogFactory.getLog(StaxEventItemWriter.class);

    /**
     * default encoding.
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * default encoding.
     */
    private static final String DEFAULT_XML_VERSION = "1.0";

    /**
     * default root tag name.
     */
    private static final String DEFAULT_ROOT_TAG_NAME = "root";

    /**
     * restart data property name.
     */
    private static final String RESTART_DATA_NAME = "position";

    /**
     * restart data property name.
     */
    private static final String WRITE_STATISTICS_NAME = "record.count";

    /**
     * file system resource.
     */
    private Resource resource;

    /**
     * xml marshaller.
     */
    private Marshaller marshaller;

    /**
     * encoding to be used while reading from the resource.
     */
    private String encoding = DEFAULT_ENCODING;

    /**
     * XML version.
     */
    private String version = DEFAULT_XML_VERSION;

    /**
     * name of the root tag.
     */
    private String rootTagName = DEFAULT_ROOT_TAG_NAME;

    /**
     * namespace prefix of the root tag.
     */
    private String rootTagNamespacePrefix = "";

    /**
     * namespace of the root tag.
     */
    private String rootTagNamespace = "";

    /**
     * root element attributes.
     */
    private Map<String, String> rootElementAttributes = null;

    /**
     * TRUE means, that output file will be overwritten if exists - default is
     * TRUE.
     */
    private boolean overwriteOutput = true;

    /**
     * file channel.
     */
    private FileChannel channel;

    /**
     *  wrapper for XML event writer that swallows StartDocument and EndDocument
     *  events.
     */
    private XMLEventWriter eventWriter;

    /**
     * XML event writer.
     */
    private XMLEventWriter delegateEventWriter;

    /**
     * current count of processed records.
     */
    private long currentRecordCount = 0;

    /**
     *
     */
    private boolean saveState = true;

    /**
     *
     */
    private StaxWriterCallback headerCallback;

    /**
     *
     */
    private StaxWriterCallback footerCallback;

    /**
     *
     */
    private Writer bufferedWriter;

    /**
     *
     */
    private boolean transactional = true;

    /**
     *
     */
    public StaxEventItemWriter() {
        setName(ClassUtils.getShortName(StaxEventItemWriter.class));
    }

    /**
     * Set output file.
     *
     * @param newResource
     *            the output file
     */
    public final void setResource(final Resource newResource) {
        this.resource = newResource;
    }

    /**
     * Set Object to XML marshaller.
     *
     * @param newMarshaller
     *            the Object to XML marshaller
     */
    public final void setMarshaller(final Marshaller newMarshaller) {
        this.marshaller = newMarshaller;
    }

    /**
     * @param newHeaderCallback is called before writing any items.
     */
    public final void setHeaderCallback(
            final StaxWriterCallback newHeaderCallback) {
        this.headerCallback = newHeaderCallback;
    }

    /**
     * @param newFooterCallback
     *            is called after writing all items but before closing the file
     */
    public final void setFooterCallback(
            final StaxWriterCallback newFooterCallback) {
        this.footerCallback = newFooterCallback;
    }

    /**
     * Flag to indicate that writes should be deferred to the end of a
     * transaction if present. Defaults to true.
     *
     * @param isTransactional
     *            the flag to set
     */
    public final void setTransactional(final boolean isTransactional) {
        this.transactional = isTransactional;
    }

    /**
     * Get used encoding.
     *
     * @return the encoding used
     */
    public final String getEncoding() {
        return encoding;
    }

    /**
     * Set encoding to be used for output file.
     *
     * @param newEncoding
     *            the encoding to be used
     */
    public final void setEncoding(final String newEncoding) {
        this.encoding = newEncoding;
    }

    /**
     * Get XML version.
     *
     * @return the XML version used
     */
    public final String getVersion() {
        return version;
    }

    /**
     * Set XML version to be used for output XML.
     *
     * @param newVersion
     *            the XML version to be used
     */
    public final void setVersion(final String newVersion) {
        this.version = newVersion;
    }

    /**
     * Get the tag name of the root element.
     *
     * @return the root element tag name
     */
    public final String getRootTagName() {
        return rootTagName;
    }

    /**
     * Set the tag name of the root element. If not set, default name is used
     * ("root"). Namespace URI and prefix can also be set optionally using the
     * notation:
     *
     * <pre>
     * {uri}prefix:root
     * </pre>
     *
     * The prefix is optional (defaults to empty), but if it is specified then
     * the uri must be provided. In addition you might want to declare other
     * namespaces using the {@link #setRootElementAttributes(Map) root
     * attributes}.
     *
     * @param newRootTagName
     *            the tag name to be used for the root element
     */
    public final void setRootTagName(final String newRootTagName) {
        this.rootTagName = newRootTagName;
    }

    /**
     * Get the namespace prefix of the root element. Empty by default.
     *
     * @return the rootTagNamespacePrefix
     */
    public final String getRootTagNamespacePrefix() {
        return rootTagNamespacePrefix;
    }

    /**
     * Get the namespace of the root element.
     *
     * @return the rootTagNamespace
     */
    public final String getRootTagNamespace() {
        return rootTagNamespace;
    }

    /**
     * Get attributes of the root element.
     *
     * @return attributes of the root element
     */
    public final Map<String, String> getRootElementAttributes() {
        return rootElementAttributes;
    }

    /**
     * Set the root element attributes to be written. If any of the key names
     * begin with "xmlns:" then they are treated as namespace declarations.
     *
     * @param newRootElementAttributes
     *            attributes of the root element
     */
    public final void setRootElementAttributes(
            final Map<String, String> newRootElementAttributes) {
        this.rootElementAttributes = newRootElementAttributes;
    }

    /**
     * Set "overwrite" flag for the output file. Flag is ignored when output
     * file processing is restarted.
     *
     * @param doOverwriteOutput Overwrite the output
     */
    public final void setOverwriteOutput(final boolean doOverwriteOutput) {
        this.overwriteOutput = doOverwriteOutput;
    }

    /**
     *
     * @param doSaveState Save the state
     */
    public final void setSaveState(final boolean doSaveState) {
        this.saveState = doSaveState;
    }

    /**
     * @throws Exception if there is a problem initializing the writer
     */
    public final void afterPropertiesSet() throws Exception {
        Assert.notNull(marshaller);
        if (rootTagName.contains("{")) {
            rootTagNamespace = rootTagName.replaceAll("\\{(.*)\\}.*", "$1");
            rootTagName = rootTagName.replaceAll("\\{.*\\}(.*)", "$1");
            if (rootTagName.contains(":")) {
                rootTagNamespacePrefix = rootTagName
                        .replaceAll("(.*):.*", "$1");
                rootTagName = rootTagName.replaceAll(".*:(.*)", "$1");
            }
        }
    }

    /**
     * Open the output source.
     * @param newExecutionContext Set the execution context
     * @see org.springframework.batch.item.ItemStream#open(ExecutionContext)
     */
    public final void open(final ExecutionContext newExecutionContext) {

        Assert.notNull(resource, "The resource must be set");

        long startAtPosition = 0;
        boolean restarted = false;

        // if restart data is provided, restart from provided offset
        // otherwise start from beginning
        if (newExecutionContext.containsKey(getKey(RESTART_DATA_NAME))) {
            startAtPosition = newExecutionContext
                    .getLong(getKey(RESTART_DATA_NAME));
            restarted = true;
        }

        open(startAtPosition, restarted);

        if (startAtPosition == 0) {
            try {
                if (headerCallback != null) {
                    headerCallback.write(delegateEventWriter);
                }
            } catch (IOException e) {
                throw new ItemStreamException("Failed to write headerItems", e);
            }
        }

    }

    /**
     * Helper method for opening output source at given file position.
     * @param position Set the position
     * @param restarted Is this execution being restarted
     */
    private void open(final long position, final boolean restarted) {

        File file;
        FileOutputStream os = null;

        try {
            file = resource.getFile();
            FileUtils.setUpOutputFile(file, restarted, overwriteOutput);
            Assert.state(resource.exists(), "Output resource must exist");
            os = new FileOutputStream(file, true);
            channel = os.getChannel();
            setPosition(position);
        } catch (IOException ioe) {
            throw new DataAccessResourceFailureException(
              "Unable to write to file resource: [" + resource + "]", ioe);
        }

        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        if (outputFactory
                .isPropertySupported("com.ctc.wstx.automaticEndElements")) {
            // If the current XMLOutputFactory implementation is supplied by
            // Woodstox >= 3.2.9 we want to disable its
            // automatic end element feature (see:
            // http://jira.codehaus.org/browse/WSTX-165) per
            // http://jira.springframework.org/browse/BATCH-761.
            outputFactory.setProperty("com.ctc.wstx.automaticEndElements",
                    Boolean.FALSE);
        }

        try {
            if (transactional) {
                bufferedWriter = new TransactionAwareBufferedWriter(
                        new OutputStreamWriter(os, encoding), new Runnable() {
                            public void run() {
                                closeStream();
                            }
                        });
            } else {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(os,
                        encoding));
            }
            delegateEventWriter = outputFactory
                    .createXMLEventWriter(bufferedWriter);
            eventWriter = new NoStartEndDocumentStreamWriter(
                    delegateEventWriter);
            if (!restarted) {
                startDocument(delegateEventWriter);
            }
        } catch (XMLStreamException xse) {
            throw new DataAccessResourceFailureException(
                "Unable to write to file resource: [" + resource + "]", xse);
        } catch (UnsupportedEncodingException e) {
            throw new DataAccessResourceFailureException(
                    "Unable to write to file resource: [" + resource
                            + "] with encoding=[" + encoding + "]", e);
        }

    }

    /**
     * Writes simple XML header containing:
     * <ul>
     * <li>xml declaration - defines encoding and XML version</li>
     * <li>opening tag of the root element and its attributes</li>
     * </ul>
     * If this is not sufficient for you, simply override this method. Encoding,
     * version and root tag name can be retrieved with corresponding getters.
     *
     * @param writer
     *            XML event writer
     * @throws XMLStreamException if there is a problem starting the document
     */
    protected final void startDocument(final XMLEventWriter writer)
            throws XMLStreamException {

        XMLEventFactory factory = XMLEventFactory.newInstance();

        // write start document
        writer.add(factory.createStartDocument(getEncoding(), getVersion()));

        // write root tag
        writer.add(factory.createStartElement(getRootTagNamespacePrefix(),
                getRootTagNamespace(), getRootTagName()));
        if (StringUtils.hasText(getRootTagNamespace())) {
            if (StringUtils.hasText(getRootTagNamespacePrefix())) {
                writer.add(factory.createNamespace(getRootTagNamespacePrefix(),
                        getRootTagNamespace()));
            } else {
                writer.add(factory.createNamespace(getRootTagNamespace()));
            }
        }

        // write root tag attributes
        if (!CollectionUtils.isEmpty(getRootElementAttributes())) {

            for (Map.Entry<String, String> entry : getRootElementAttributes()
                    .entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("xmlns")) {
                    String prefix = "";
                    if (key.contains(":")) {
                        prefix = key.substring(key.indexOf(":") + 1);
                    }
                    writer.add(
                            factory.createNamespace(prefix, entry.getValue()));
                } else {
                    writer.add(factory.createAttribute(key, entry.getValue()));
                }
            }

        }

        /*
         * This forces the flush to write the end of the root element and avoids
         * an off-by-one error on restart.
         */
        writer.add(factory.createIgnorableSpace(""));
        writer.flush();

    }

    /**
     * Writes the EndDocument tag manually.
     *
     * @param writer
     *            XML event writer
     * @throws XMLStreamException if there is a problem ending the document
     */
    protected final void endDocument(final XMLEventWriter writer)
            throws XMLStreamException {

        // writer.writeEndDocument(); <- this doesn't work after restart
        // we need to write end tag of the root element manually

        String nsPrefix = null;
        if (!StringUtils.hasText(getRootTagNamespacePrefix())) {
            nsPrefix = "";
        } else {
            nsPrefix = getRootTagNamespacePrefix() + ":";
        }
        try {
            bufferedWriter.write("</" + nsPrefix + getRootTagName() + ">");
        } catch (IOException ioe) {
            throw new DataAccessResourceFailureException(
                    "Unable to close file resource: [" + resource + "]", ioe);
        }
    }

    /**
     * Flush and close the output source.
     *
     * @see org.springframework.batch.item.ItemStream#close()
     */
    public final void close() {

        // harmless event to close the root tag if there were no items
        XMLEventFactory factory = XMLEventFactory.newInstance();
        try {
            delegateEventWriter.add(factory.createCharacters(""));
        } catch (XMLStreamException e) {
            log.error(e);
        }

        try {
            if (footerCallback != null) {
                footerCallback.write(delegateEventWriter);
            }
            delegateEventWriter.flush();
            endDocument(delegateEventWriter);
        } catch (IOException e) {
            throw new ItemStreamException("Failed to write footer items", e);
        } catch (XMLStreamException e) {
            throw new ItemStreamException(
                    "Failed to write end document tag", e);
        } finally {

            try {
                eventWriter.close();
            } catch (XMLStreamException e) {
                log.error("Unable to close file resource: [" + resource + "] "
                        + e);
            } finally {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    log.error("Unable to close file resource: [" + resource
                            + "] " + e);
                } finally {
                    if (!transactional) {
                        closeStream();
                    }
                }
            }
        }
    }

    /**
     *
     */
    private void closeStream() {
        try {
            channel.close();
        } catch (IOException ioe) {
            log.error("Unable to close file resource: [" + resource + "] "
                    + ioe);
        }
    }

    /**
     * Write the value objects and flush them to the file.
     *
     * @param items
     *            the value object
     * @throws IOException
     *             if there is a problem writing to the resource
     */
    public final void write(final List<? extends T> items) throws IOException {

        currentRecordCount += items.size();

        for (Object object : items) {
            Assert.state(marshaller.supports(object.getClass()),
                "Marshaller must support the class of the marshalled object");
            // CHANGE - needed to work with Spring core 3.0.1 (incompatibility
            // between spring core 3.0.1 and spring ws oxm 1.5.9)
            // marshaller.marshal(object, new StaxResult(eventWriter));
            marshaller.marshal(object, StaxUtils.createStaxResult(eventWriter));
            // END CHANGE
        }
        try {
            eventWriter.flush();
        } catch (XMLStreamException e) {
            throw new WriteFailedException("Failed to flush the events", e);
        }

    }

    /**
     * @param executionContext Set the execution context
     * Get the restart data.
     *
     * @see org.springframework.batch.item.ItemStream#update(ExecutionContext)
     */
    public final void update(final ExecutionContext executionContext) {

        if (saveState) {
            Assert.notNull(executionContext,
                    "ExecutionContext must not be null");
            executionContext.putLong(getKey(RESTART_DATA_NAME), getPosition());
            executionContext.putLong(getKey(WRITE_STATISTICS_NAME),
                    currentRecordCount);
        }
    }

    /**
     * Get the actual position in file channel. This method flushes any buffered
     * data before position is read.
     *
     * @return byte offset in file channel
     */
    private long getPosition() {

        long position;

        try {
            eventWriter.flush();
            position = channel.position();
            if (bufferedWriter instanceof TransactionAwareBufferedWriter) {
                position += ((TransactionAwareBufferedWriter) bufferedWriter)
                        .getBufferSize();
            }
        } catch (Exception e) {
            throw new DataAccessResourceFailureException(
                    "Unable to write to file resource: [" + resource + "]", e);
        }

        return position;
    }

    /**
     * Set the file channel position.
     *
     * @param newPosition
     *            new file channel position
     */
    private void setPosition(final long newPosition) {

        try {
            channel.truncate(newPosition);
            channel.position(newPosition);
        } catch (IOException e) {
            throw new DataAccessResourceFailureException(
                    "Unable to write to file resource: [" + resource + "]", e);
        }

    }

}
