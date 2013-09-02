package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.springframework.batch.core.JobExecutionException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

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
