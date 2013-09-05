package org.emonocot.model.marshall.json;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameter.ParameterType;
import org.springframework.batch.core.JobParameters;

/**
 *
 * @author ben
 *
 */
public class JobInstanceDeserializer extends JsonDeserializer<JobInstance> {

    @Override
    public final JobInstance deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        JsonToken jsonToken = jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "id";
        jsonParser.nextToken();
        long jobId = jsonParser.getLongValue();
        jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "jobName";
        jsonParser.nextToken();
        String jobName = jsonParser.getText();
        jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "version";
        jsonParser.nextToken();
        Integer version = jsonParser.getValueAsInt();
        jsonParser.nextToken();
        assert jsonParser.getCurrentName() == "parameters";
        jsonToken = jsonParser.nextToken();
        assert jsonToken == JsonToken.START_ARRAY;
        Map<String, JobParameter> jobParameterMap = new HashMap<String, JobParameter>();
		while (jsonToken != JsonToken.END_ARRAY) {
			jsonToken = jsonParser.nextToken();
			if (jsonToken == JsonToken.END_ARRAY) {

			} else {
				assert jsonToken == JsonToken.START_OBJECT;
				jsonToken = jsonParser.nextToken();
				assert jsonParser.getCurrentName() == "name";
				jsonToken = jsonParser.nextToken();
				String parameterName = jsonParser.getText();
				jsonToken = jsonParser.nextToken();
				assert jsonParser.getCurrentName() == "type";
				jsonToken = jsonParser.nextToken();
				ParameterType type = ParameterType
						.valueOf(jsonParser.getText());
				jsonToken = jsonParser.nextToken();
				assert jsonParser.getCurrentName() == "value";
				jsonToken = jsonParser.nextToken();
				JobParameter jobParameter = null;

				switch (type) {
				case DATE:
					jobParameter = new JobParameter(new Date(
							jsonParser.getLongValue()));
					break;
				case LONG:
					jobParameter = new JobParameter(jsonParser.getLongValue());
					break;
				case DOUBLE:
					jobParameter = new JobParameter(jsonParser.getDoubleValue());
					break;
				case STRING:
				default:
					jobParameter = new JobParameter(jsonParser.getText());
				}
				jobParameterMap.put(parameterName, jobParameter);
				jsonToken = jsonParser.nextToken();
				assert jsonToken == jsonToken.END_OBJECT;

				jsonToken = jsonParser.nextToken();
			}
		}

        JobParameters jobParameters = new JobParameters(jobParameterMap);
        JobInstance jobInstance = new JobInstance(jobId, jobParameters, jobName);
        jobInstance.setVersion(version);
        return jobInstance;
    }

}
