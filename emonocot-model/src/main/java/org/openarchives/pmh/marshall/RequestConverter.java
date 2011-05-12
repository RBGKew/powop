package org.openarchives.pmh.marshall;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.openarchives.pmh.Request;
import org.openarchives.pmh.SetSpec;
import org.openarchives.pmh.Verb;
import org.openarchives.pmh.MetadataPrefix;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 *
 * @author ben
 *
 */
public class RequestConverter implements Converter {
    /**
     *
     */
    private static final DateTimeFormatter PRINTER = ISODateTimeFormat
            .dateTime();
    /**
     *
     */
    private static final DateTimeFormatter PARSER = ISODateTimeFormat
            .dateTimeParser();

    @Override
    public final boolean canConvert(final Class clazz) {
       if (clazz != null && clazz.equals(Request.class)) {
           return true;
       }
       return false;
    }

    @Override
    public final void marshal(final Object value,
            final HierarchicalStreamWriter writer,
            final MarshallingContext context) {

        Request request = (Request) value;
        if (request.getFrom() != null) {
            writer.addAttribute("from",
                    RequestConverter.PRINTER.print(request.getFrom()));
        }

        if (request.getUntil() != null) {
            writer.addAttribute("until",
                    RequestConverter.PRINTER.print(request.getUntil()));
        }

        if (request.getVerb() != null) {
            writer.addAttribute("verb", request.getVerb().toString());
        }

        if (request.getMetadataPrefix() != null) {
            writer.addAttribute("metadataPrefix", request.getMetadataPrefix()
                    .value());
        }

        if (request.getSet() != null) {
            writer.addAttribute("set", request.getSet().getValue());
        }

        if (request.getIdentifier() != null) {
            writer.addAttribute("identifier", request.getIdentifier());
        }

        if (request.getResumptionToken() != null) {
            writer.addAttribute("resumptionToken",
                request.getResumptionToken());
        }
        writer.setValue(request.getValue());
    }

    @Override
    public final Object unmarshal(final HierarchicalStreamReader reader,
            final UnmarshallingContext context) {
        Request request = new Request();
        if (reader.getAttribute("from") != null) {
            request.setFrom(
                    RequestConverter.PARSER.parseDateTime(
                            reader.getAttribute("from")));
        }

        if (reader.getAttribute("until") != null) {
            request.setUntil(
                    RequestConverter.PARSER.parseDateTime(
                            reader.getAttribute("until")));
        }

        if (reader.getAttribute("verb") != null) {
            request.setVerb(Verb.valueOf(reader.getAttribute("verb")));
        }

        if (reader.getAttribute("metadataPrefix") != null) {
            request.setMetadataPrefix(
                    MetadataPrefix.fromValue(
                            reader.getAttribute("metadataPrefix")));
        }

        if (reader.getAttribute("set") != null) {
            request.setSet(new SetSpec(reader.getAttribute("set")));
        }

        if (reader.getAttribute("identifier") != null) {
            request.setIdentifier(reader.getAttribute("identifier"));
        }

        if (reader.getAttribute("resumptionToken") != null) {
            request.setResumptionToken(reader.getAttribute("resumptionToken"));
        }

        request.setResumptionToken(reader.getValue());

        return request;
    }
}
