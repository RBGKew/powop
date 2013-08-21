package org.emonocot.job.iucn;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 *
 */
public class CheckEmptyResponseTaskletTest {
	
	Resource responseWithData = new ClassPathResource("org/emonocot/job/common/iucn.json");
	
	Resource emptyResponse = new ClassPathResource("org/emonocot/job/common/empty.json");
	
	StepContribution stepContribution;
	
	ChunkContext chunkContext;
	
	ExitStatus emptyResponseExitStatus;

    private CheckEmptyResponseTasklet checkEmptyResponseTasklet;

    @Before
    public final void setUp() {
    	stepContribution = EasyMock.createMock(StepContribution.class);
    	chunkContext = EasyMock.createMock(ChunkContext.class);
        checkEmptyResponseTasklet = new CheckEmptyResponseTasklet();
        emptyResponseExitStatus = new ExitStatus("EMPTY_RESPONSE").addExitDescription("The webservice returned an empty list of taxa");
    }

    @Test
    public final void checkResponseWithData() throws Exception {
    	checkEmptyResponseTasklet.setInputFile(responseWithData);
    	EasyMock.replay(stepContribution,chunkContext);
        assertEquals("CheckEmptyResponse should return RepeatStatus.FINISHED",
        		checkEmptyResponseTasklet.execute(stepContribution, chunkContext), RepeatStatus.FINISHED);
        EasyMock.verify(stepContribution,chunkContext);
    }
    
    @Test
    public final void checkEmptyResponse() throws Exception {
    	checkEmptyResponseTasklet.setInputFile(emptyResponse);
    	stepContribution.setExitStatus(EasyMock.eq(emptyResponseExitStatus));
    	EasyMock.replay(stepContribution,chunkContext);
        
    	assertEquals("CheckEmptyResponse should return RepeatStatus.FINISHED",
        		checkEmptyResponseTasklet.execute(stepContribution, chunkContext), RepeatStatus.FINISHED);
        EasyMock.verify(stepContribution,chunkContext);
    }

}
