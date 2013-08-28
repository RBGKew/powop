package org.emonocot.job.key;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 *
 */
public class XmlTransformingTasklet implements Tasklet {
	
	private static Logger logger = LoggerFactory.getLogger(XmlTransformingTasklet.class);

    private Resource inputFile;

    private Resource xsltFile;

    private Resource outputFile;
    
    private ErrorListener errorListener;
    
    public void setErrorListener(ErrorListener errorListener) {
    	this.errorListener = errorListener;
    }

    /**
     * @param newInputFile the inputFile to set
     */
    public final void setInputFile(final Resource newInputFile) {
        this.inputFile = newInputFile;
    }

    /**
     * @param newXsltFile the xsltFile to set
     */
    public final void setXsltFile(final Resource newXsltFile) {
        this.xsltFile = newXsltFile;
    }

    /**
     * @param newOutputFile the outputFile to set
     */
    public final void setOutputFile(final Resource newOutputFile) {
        this.outputFile = newOutputFile;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    /**
     *
     */
    private Map<String, String> parameters = new HashMap<String, String>();

    /**
     * @param contribution
     *            Set the step contribution
     * @param chunkContext
     *            Set the chunk context
     * @return the repeat status
     * @throws Exception
     *             if there is a problem
     */
    public final RepeatStatus execute(final StepContribution contribution,
            final ChunkContext chunkContext) throws Exception {
        // Set up input documents
        Source inputXML = new StreamSource(inputFile.getFile());

        Source inputXSL = new StreamSource(xsltFile.getFile());

        // Set up output sink
        Result outputXHTML = new StreamResult(outputFile.getFile());

        // Setup a factory for transforms
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(inputXSL);
        for (String parameterName : parameters.keySet()) {
            transformer.setParameter(parameterName,
                    parameters.get(parameterName));
        }
        if(errorListener != null) {
            transformer.setErrorListener(errorListener);
        }
        transformer.transform(inputXML, outputXHTML);
        return RepeatStatus.FINISHED;
    }
}
