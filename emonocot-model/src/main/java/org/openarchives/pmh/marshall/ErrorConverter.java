package org.openarchives.pmh.marshall;

import org.openarchives.pmh.Error;
import org.openarchives.pmh.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ErrorConverter implements Converter {
    /**
    *
    */
   private static Logger logger
       = LoggerFactory.getLogger(ErrorConverter.class);

    public final boolean canConvert(final Class clazz) {
       if (clazz != null && clazz.equals(Error.class)) {
           return true;
       }
       return false;
    }

    public final void marshal(final Object value,
            final HierarchicalStreamWriter writer,
            final MarshallingContext context) {

        Error error = (Error) value;
        if (error.getCode() != null) {
            logger.debug(error.getCode().toString());
            writer.addAttribute("code", error.getCode().toString());
        }

        if (error.getValue() != null) {
            logger.debug(error.getValue());
            writer.setValue(error.getValue());
        }
    }

    public final Object unmarshal(final HierarchicalStreamReader reader,
            final UnmarshallingContext context) {
        Error error = new Error();
        error.setCode(ErrorCode.valueOf(reader.getAttribute("code")));

        error.setValue(reader.getValue());

        return error;
    }
}
