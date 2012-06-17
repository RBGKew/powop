package org.emonocot.portal.integration;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobLaunchResponse;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;


/**
 *
 * @author ben
 *
 */
public class JobLaunchMessageConverter implements MessageConverter {
	
	/**
    *
    */
   private ObjectMapper objectMapper;

   /**
    *
    * @param objectMapper Set the object mapper
    */
   public final void setObjectMapper(final ObjectMapper objectMapper) {
       this.objectMapper = objectMapper;
   }

    /**
     *
     * @param jobLaunchRequest Set the job launch request
     * @return the job launch request as a string
     */
    public final String print(final JobLaunchRequest jobLaunchRequest) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(jobLaunchRequest.getJob().getName());
        stringBuffer.append("[");
        for (String parameterName : jobLaunchRequest.getJobParameters()
                .getParameters().keySet()) {
            stringBuffer.append(parameterName
                    + "="
                    + jobLaunchRequest.getJobParameters().getParameters()
                            .get(parameterName).getValue());
            stringBuffer.append(", ");
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
    
    public final Object parse(final JobExecutionInfo jobExecutionInfo) {
    	System.out.println("RECIEVED: " + jobExecutionInfo);    	
    	return jobExecutionInfo;
    }
    
    
    /**
    *
    * @param string the message
    * @return the job launch response
    * @throws JobExecutionException if the response is not expected
    */
   public final Object parse(final String string)
           throws JobExecutionException {

           try {
               JobLaunchResponse response = objectMapper.readValue(string, JobLaunchResponse.class);
               return response.getJobExecution();
           } catch (IllegalArgumentException e) {
               try {
                   JobExecutionException jobExecutionException = objectMapper
                           .readValue(string, JobExecutionException.class);
                   throw jobExecutionException;
               } catch (IllegalArgumentException e1) {
                   throw new JobExecutionException("Could not parse response",
                           e1);
               } catch (JsonParseException jpe) {
                   throw new JobExecutionException("Could not parse response",
                           jpe);
               } catch (JsonMappingException jme) {
                   throw new JobExecutionException("Could not parse response",
                           jme);
               } catch (IOException ioe) {
                   throw new JobExecutionException("Could not parse response",
                           ioe);
               }
           } catch (Exception e) {
               throw new JobExecutionException("Could not parse response", e);
           }
   }

	@Override
	public Object fromMessage(Message message) throws JMSException,
			MessageConversionException {
		TextMessage textMessage = (TextMessage) message;
		try {
			return parse(textMessage.getText());
		} catch (JobExecutionException jee) {
			throw new MessageConversionException(jee.getLocalizedMessage(), jee);
		}
	}

	@Override
	public Message toMessage(Object object, Session session)
			throws JMSException, MessageConversionException {
		JobLaunchRequest jobLaunchRequest = (JobLaunchRequest) object;
		TextMessage textMessage = session.createTextMessage();
		textMessage.setText(print(jobLaunchRequest));
		System.out.println("SENDING: " + print(jobLaunchRequest));
		return textMessage;
	}
}
