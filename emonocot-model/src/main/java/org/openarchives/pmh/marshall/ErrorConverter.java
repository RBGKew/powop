package org.openarchives.pmh.marshall;

import org.openarchives.pmh.Error;
import org.openarchives.pmh.ErrorCode;

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

    @Override
    public final boolean canConvert(final Class clazz) {
       if (clazz != null && clazz.equals(Error.class)) {
           return true;
       }
       return false;
    }

    @Override
    public final void marshal(final Object value,
            final HierarchicalStreamWriter writer,
            final MarshallingContext context) {

        Error error = (Error) value;
        writer.addAttribute("code", error.getCode().toString());
        writer.setValue(error.getValue());
    }

    @Override
    public final Object unmarshal(final HierarchicalStreamReader reader,
            final UnmarshallingContext context) {
        Error error = new Error();
        error.setCode(ErrorCode.valueOf(reader.getAttribute("code")));

        error.setValue(reader.getValue());

        return error;
    }
}
