package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.api.job.JobExecutionInfo;
import org.springframework.batch.core.JobExecutionException;

/**
 *
 * @author ben
 *
 */
public class JobExecutionExceptionDeserializer extends
        JsonDeserializer<JobExecutionException> {

    /**
     * @param jsonParser Set the json parser
     * @param deserializationContext Set the deserialization context
     * @return a JobExecutionInfo object
     * @throws IOException if there is a problem
     */
    @Override
    public final JobExecutionException deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        try {
            JsonToken jsonToken = jsonParser.nextToken();
            assert jsonParser.getCurrentName() == "errors";
            jsonToken = jsonParser.nextToken();
            assert jsonToken == JsonToken.START_OBJECT;
            jsonToken = jsonParser.nextToken();
            // error code
            jsonToken = jsonParser.nextToken();
            JobExecutionException jobExecutionException = new JobExecutionException(
                    jsonParser.getText());
            jsonToken = jsonParser.nextToken();
            while (jsonToken != JsonToken.END_OBJECT) {
                jsonToken = jsonParser.nextToken();
            }
            return jobExecutionException;
        } catch (AssertionError ae) {
            throw new IllegalArgumentException("Cannot convert into JobExecutionException");
        }
    }

}
