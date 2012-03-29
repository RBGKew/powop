package org.emonocot.model.marshall.json;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.module.SimpleDeserializers;
import org.codehaus.jackson.map.module.SimpleKeyDeserializers;
import org.codehaus.jackson.map.module.SimpleSerializers;
import org.emonocot.api.JobInstanceService;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.model.geography.GeographicalRegion;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobInstance;

/**
 *
 * @author ben
 *
 */
public class CustomModule extends Module {

    /**
     *
     */
    private JobInstanceService jobInstanceService;

    /**
     *
     * @param newJobInstanceService Set the job instance service
     */
    public CustomModule(final JobInstanceService newJobInstanceService) {
        this.jobInstanceService = newJobInstanceService;
    }

    @Override
    public final String getModuleName() {
        return "eMonocotModule";
    }

    @Override
    public final void setupModule(final SetupContext setupContext) {
        SimpleKeyDeserializers keyDeserializers = new SimpleKeyDeserializers();
        keyDeserializers.addDeserializer(GeographicalRegion.class,
                new GeographicalRegionKeyDeserializer());
        setupContext.addKeyDeserializers(keyDeserializers);
        SimpleSerializers simpleSerializers = new SimpleSerializers();
        simpleSerializers.addSerializer(new JobInstanceSerializer());
        simpleSerializers.addSerializer(new JobExecutionSerializer());
        setupContext.addSerializers(simpleSerializers);

        SimpleDeserializers simpleDeserializers = new SimpleDeserializers();
        simpleDeserializers.addDeserializer(JobInstance.class,
                new JobInstanceDeserializer());
        simpleDeserializers.addDeserializer(JobExecution.class,
                new JobExecutionDeserializer(jobInstanceService));
        simpleDeserializers.addDeserializer(JobExecutionException.class,
                new JobExecutionExceptionDeserializer());
        setupContext.addDeserializers(simpleDeserializers);
    }

    @Override
    public final Version version() {
        return new Version(0, 1, 0, "alpha");
    }

}
