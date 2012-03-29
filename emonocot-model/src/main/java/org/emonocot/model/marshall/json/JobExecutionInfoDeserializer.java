package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobInstanceInfo;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.batch.core.BatchStatus;

/**
 *
 * @author ben
 *
 */
public class JobExecutionInfoDeserializer extends
        JsonDeserializer<JobExecutionInfo> {
    /**
     *
     */
    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss");

    /**
     * @param jsonParser
     *            Set the json parser
     * @param deserializationContext
     *            Set the deserialization context
     * @return a JobExecutionInfo object
     * @throws IOException
     *             if there is a problem
     */
    @Override
    public final JobExecutionInfo deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
        JsonToken jsonToken = null;
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonToken = jsonParser.nextToken();
            if (fieldName == "resource") {
                jobExecutionInfo.setResource(jsonParser.getText());
            } else if (fieldName == "id") {
                jobExecutionInfo.setId(Long.parseLong(jsonParser.getText()));
            } else if (fieldName == "status") {
                jobExecutionInfo.setStatus(BatchStatus.valueOf(jsonParser
                        .getText()));
            } else if (fieldName == "startTime") {
                jobExecutionInfo.setStartTime(dateTimeFormatter
                        .parseDateTime(jsonParser.getText()));
            } else if (fieldName == "duration") {
                jobExecutionInfo.setDuration(dateTimeFormatter
                        .parseDateTime(jsonParser.getText()));
            } else if (fieldName == "exitCode") {
                jobExecutionInfo.setExitCode(jsonParser.getText());
            } else if (fieldName == "exitDescription") {
                jobExecutionInfo.setExitDescription(jsonParser.getText());
            } else if (fieldName == "jobInstance") {
                assert jsonToken == JsonToken.START_OBJECT;
                jsonToken = jsonParser.nextToken();
                assert jsonParser.getCurrentName() == "resource";
                jsonToken = jsonParser.nextToken();
                JobInstanceInfo jobInstanceInfo = new JobInstanceInfo();
                jobInstanceInfo.setResource(jsonParser.getText());
                jobExecutionInfo.setJobInstance(jobInstanceInfo);
                jsonToken = jsonParser.nextToken();
                assert jsonToken == JsonToken.END_OBJECT;
            } else if (fieldName == "stepExecutions") {
                assert jsonToken == JsonToken.START_OBJECT;
                jsonParser.nextToken();
                while (jsonToken != JsonToken.END_OBJECT) {
                    String stepName = jsonParser.getCurrentName();
                    jsonToken = jsonParser.nextToken();
                    assert jsonToken == JsonToken.START_OBJECT;
                    jsonToken = jsonParser.nextToken();
                    while (jsonToken != JsonToken.END_OBJECT) {
                        jsonToken = jsonParser.nextToken();
                    }
                }
            }
        }
        return jobExecutionInfo;
    }
}
