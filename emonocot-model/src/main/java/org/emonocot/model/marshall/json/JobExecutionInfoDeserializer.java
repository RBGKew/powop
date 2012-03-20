package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.api.job.JobExecutionInfo;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;

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
     * @param jsonParser Set the json parser
     * @param deserializationContext Set the deserialization context
     * @return a JobExecutionInfo object
     * @throws IOException if there is a problem
     */
    @Override
    public final JobExecutionInfo deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        try {
        JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
        JsonToken jsonToken = jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "jobExecution";
        jsonToken = jsonParser.nextToken();
        assert jsonToken == JsonToken.START_OBJECT;
        jsonToken = jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "resource";
        jsonToken = jsonParser.nextToken();
        jobExecutionInfo.setResource(jsonParser.getText());
        jsonToken = jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "id";
        jsonToken =  jsonParser.nextToken();
        jobExecutionInfo.setId(Integer.parseInt(jsonParser.getText()));
        jsonToken = jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "status";
        jsonToken = jsonParser.nextToken();
        jobExecutionInfo.setStatus(BatchStatus.valueOf(jsonParser.getText()));
        jsonToken = jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "startTime";
        jsonToken = jsonParser.nextToken();
        jobExecutionInfo.setStartTime(dateTimeFormatter.parseDateTime(jsonParser.getText()));
        jsonToken = jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "duration";
        jsonToken = jsonParser.nextToken();
        jobExecutionInfo.setDuration(dateTimeFormatter.parseDateTime(jsonParser.getText()));
        jsonToken = jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "exitCode";
        jsonToken = jsonParser.nextToken();
        jobExecutionInfo.setExitStatus(new ExitStatus(jsonParser.getText()));
        jsonToken = jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "exitDescription";
        jsonToken = jsonParser.nextToken();
        jobExecutionInfo.setExitDescription(jsonParser.getText());
        jsonToken = jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "jobInstance";
        jsonToken = jsonParser.nextToken();
        assert jsonToken == JsonToken.START_OBJECT;
        jsonToken = jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "resource";
        jsonToken = jsonParser.nextToken();
        jobExecutionInfo.setJobInstance(jsonParser.getText());
        jsonToken = jsonParser.nextToken();
        assert jsonToken == JsonToken.END_OBJECT;
        jsonToken = jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "stepExecutions";
        jsonToken = jsonParser.nextToken();
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
        return jobExecutionInfo;
        } catch (AssertionError ae) {
            throw new IllegalArgumentException("JSON is not in the expected format");
        }
    }

}
