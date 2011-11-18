package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.batch.core.JobExecution;

/**
 *
 * @author ben
 *
 */
public class JobExecutionSerializer extends JsonSerializer<JobExecution> {

    @Override
    public final Class<JobExecution> handledType() {
        return JobExecution.class;
    }

    @Override
    public final void serialize(final JobExecution jobExecution,
            final JsonGenerator jsonGenerator,
            final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("id");
        jsonGenerator.writeNumber(jobExecution.getId());
        jsonGenerator.writeFieldName("jobInstance");
        jsonGenerator.writeNumber(jobExecution.getJobId());
        jsonGenerator.writeEndObject();
    }

}
