package org.emonocot.model.marshall.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.emonocot.api.JobInstanceService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;

/**
 *
 * @author ben
 *
 */
public class JobExecutionDeserializer extends JsonDeserializer<JobExecution> {

    /**
     *
     */
    private JobInstanceService jobInstanceService;

    /**
     *
     * @param newJobInstanceService Set the job instance service
     */
    public JobExecutionDeserializer(
            final JobInstanceService newJobInstanceService) {
        this.jobInstanceService = newJobInstanceService;
    }

    @Override
    public final JobExecution deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        JsonToken jsonToken = jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "id";
        jsonParser.nextToken();
        long jobId = jsonParser.getLongValue();
        jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "jobInstance";
        jsonParser.nextToken();
        long jobInstanceId = jsonParser.getLongValue();
        JobInstance jobInstance = jobInstanceService.find(jobInstanceId);
        return new JobExecution(jobInstance, jobId);
    }
}
