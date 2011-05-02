package org.emonocot.job.scratchpads.model.convert;

import org.emonocot.job.scratchpads.model.EoLAgent;

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
public class EoLAgentConverter implements Converter {

    @Override
    public final boolean canConvert(final Class clazz) {
        return clazz.equals(EoLAgent.class);
    }

    @Override
    public final void marshal(final Object value,
            final HierarchicalStreamWriter writer,
            final MarshallingContext context) {
        EoLAgent agent = (EoLAgent) value;
        writer.addAttribute("role", agent.getRole());
        writer.setValue(agent.getURI());
    }

    @Override
    public final Object unmarshal(final HierarchicalStreamReader reader,
            final UnmarshallingContext context) {
        EoLAgent agent = new EoLAgent();
        // Attributes must be got first
        agent.setRole(reader.getAttribute("role"));
        agent.setURI(reader.getValue());
        return agent;
    }
}

