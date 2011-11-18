package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;

/**
 *
 * @author ben
 *
 */
public class JobInstanceSerializer extends JsonSerializer<JobInstance> {

    @Override
    public final Class<JobInstance> handledType() {
        return JobInstance.class;
    }

    @Override
    public final void serialize(final JobInstance jobInstance,
            final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("id");
        jsonGenerator.writeNumber(jobInstance.getId());
        jsonGenerator.writeFieldName("jobName");
        jsonGenerator.writeString(jobInstance.getJobName());
        jsonGenerator.writeArrayFieldStart("parameters");
        for (String parameterName : jobInstance.getJobParameters()
                .getParameters().keySet()) {
            JobParameter jobParameter = jobInstance.getJobParameters()
                    .getParameters().get(parameterName);
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("name");
            jsonGenerator.writeString(parameterName);
            jsonGenerator.writeFieldName("type");
            jsonGenerator.writeString(jobParameter.getType().name());
            jsonGenerator.writeFieldName("value");
            jsonGenerator.writeString(jobParameter.getValue().toString());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }

}
