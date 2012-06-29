package org.emonocot.harvest.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 *
 * @author Adrian Gonzalez
 */
public class ParameterConvertingTasklet implements Tasklet {

    private final Log logger = LogFactory
            .getLog(ParameterConvertingTasklet.class);

    /**
     *
     */
    private String fieldNames;

    /**
     *
     */
    public final void setFieldNames(final String fieldNames) {
        this.fieldNames = fieldNames;
    }
    
    /**
     *
     */
    private String defaultValues;
    
    /**
     *
     */
    private String fieldNamesKey;
    
    /**
     *
     */
    private String defaultValuesKey;
    
    /**
     *
     */
    public final void setFieldNamesKey(String fieldNamesKey) {
    	this.fieldNamesKey = fieldNamesKey;
    }
    
    /**
     *
     */
    public final void setDefaultValuesKey(String defaultValuesKey) {
    	this.defaultValuesKey = defaultValuesKey;
    }    

    /**
     *
     */
    public final void setDefaultValues(final String defaultValues) {
        this.defaultValues = defaultValues;
    }


    public final RepeatStatus execute(final StepContribution contribution,
            final ChunkContext chunkContext) throws Exception {
	    Map<String,String> defaultValuesMap = new HashMap<String,String>();
        if(this.defaultValues != null) {
        	String values = defaultValues.substring(1, this.defaultValues.length() - 1);
		    for(String defaultValue : values.split(",")) {
			    if(defaultValue.indexOf("=") > -1) {
			        String field = defaultValue.substring(0,defaultValue.indexOf("="));
			        String value = defaultValue.substring(defaultValue.indexOf("=") + 1, defaultValue.length());
			        defaultValuesMap.put(field,value);
		        }
		    }
	    }
	    chunkContext.getStepContext().getStepExecution()
	        .getJobExecution().getExecutionContext().put(defaultValuesKey, defaultValuesMap);
	    System.out.println("SETTING " + defaultValuesKey + " as " + defaultValuesMap);
	    String names = fieldNames.substring(1, this.fieldNames.length() - 1);
	    String[] fieldNamesArray = names.split(",");
	    chunkContext.getStepContext().getStepExecution()
	        .getJobExecution().getExecutionContext().put(fieldNamesKey, fieldNamesArray);
	    System.out.println("SETTING " + fieldNamesKey + " as " + Arrays.toString(fieldNamesArray));
        return RepeatStatus.FINISHED;
    }
}
