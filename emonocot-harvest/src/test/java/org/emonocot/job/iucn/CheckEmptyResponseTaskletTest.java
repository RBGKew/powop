/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
