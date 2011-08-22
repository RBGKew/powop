package org.openarchives.pmh.marshall;

import java.math.BigInteger;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.openarchives.pmh.ResumptionToken;

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
public class ResumptionTokenConverter implements Converter {
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

    public final boolean canConvert(final Class clazz) {
       if (clazz != null && clazz.equals(ResumptionToken.class)) {
           return true;
       }
       return false;
    }

    public final void marshal(final Object value,
            final HierarchicalStreamWriter writer,
            final MarshallingContext context) {

        ResumptionToken resumptionToken = (ResumptionToken) value;
        if (resumptionToken.getExpirationDate() != null) {
            writer.addAttribute("expirationDate",
                    ResumptionTokenConverter.PRINTER.print(
                            resumptionToken.getExpirationDate()));
        }

        if (resumptionToken.getCompleteListSize() != null) {
            writer.addAttribute("completeListSize", resumptionToken
                    .getCompleteListSize().toString());
        }

        if (resumptionToken.getCursor() != null) {
            writer.addAttribute("cursor", resumptionToken.getCursor()
                    .toString());
        }
        if (resumptionToken.getValue() != null) {
            writer.setValue(resumptionToken.getValue());
        }
    }

    public final Object unmarshal(final HierarchicalStreamReader reader,
            final UnmarshallingContext context) {
        ResumptionToken resumptionToken = new ResumptionToken();
        if (reader.getAttribute("expirationDate") != null) {
            resumptionToken.setExpirationDate(
                    ResumptionTokenConverter.PARSER.parseDateTime(
                            reader.getAttribute("expirationDate")));
        }

        if (reader.getAttribute("completeListSize") != null) {
            resumptionToken.setCompleteListSize(
                BigInteger.valueOf(
                     Long.parseLong(reader.getAttribute("completeListSize"))));
        }

        if (reader.getAttribute("cursor") != null) {
            resumptionToken.setCursor(
                BigInteger.valueOf(
                     Long.parseLong(reader.getAttribute("cursor"))));
        }

        resumptionToken.setValue(reader.getValue());

        return resumptionToken;
    }
}
