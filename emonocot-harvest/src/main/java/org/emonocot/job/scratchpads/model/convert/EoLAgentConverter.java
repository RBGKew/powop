package org.emonocot.job.scratchpads.model.convert;

import org.emonocot.job.scratchpads.model.EoLAgent;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class EoLAgentConverter implements Converter {

	@Override
    public boolean canConvert(final Class clazz) {
        return clazz.equals(EoLAgent.class);
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        EoLAgent agent = (EoLAgent) value;
        writer.addAttribute("role", agent.getRole());
        writer.setValue(agent.getURI());
    }

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		EoLAgent agent = new EoLAgent();
		// Attributes must be got first
		agent.setRole(reader.getAttribute("role"));
		agent.setURI(reader.getValue());
		return agent;
	}
}

