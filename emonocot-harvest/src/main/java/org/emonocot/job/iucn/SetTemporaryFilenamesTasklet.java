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

import java.io.File;
import java.util.UUID;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 *
 * @author ben
 *
 */
public class SetTemporaryFilenamesTasklet implements Tasklet {

    private String harvesterSpoolDirectory;
    
	private String subtribe;
	
	private String tribe;
	
	private String subfamily;
	
	private String family;

    public void setHarvesterSpoolDirectory(String newHarvesterSpoolDirectory) {
        this.harvesterSpoolDirectory = newHarvesterSpoolDirectory;
    }
    
    public void setSubtribe(String subtribe) {
		this.subtribe = subtribe;
	}

	public void setTribe(String tribe) {
		this.tribe = tribe;
	}

	public void setSubfamily(String subfamily) {
		this.subfamily = subfamily;
	}

	public void setFamily(String family) {
		this.family = family;
	}

    /**
     * @param contribution Set the step contribution
     * @param chunkContext Set the chunk context
     * @return the repeat status
     * @throws Exception if there is a problem deleting the resources
     */
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {
        UUID uuid1 = UUID.randomUUID();
        String splitFileName = harvesterSpoolDirectory + File.separator + uuid1.toString() + ".json";
        UUID uuid2 = UUID.randomUUID();
        String temporaryFileName = harvesterSpoolDirectory + File.separator + uuid2.toString() + ".json";
        
        File temporaryFile = new File(temporaryFileName);
        File splitFile = new File(splitFileName);
        ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        
        String subsetRank = null;
		String subsetValue = null;

		if (subtribe != null) {
			subsetRank = "subtribe";
			subsetValue = subtribe;
		} else if (tribe != null) {
			subsetRank = "tribe";
			subsetValue = tribe;
		} else if (subfamily != null) {
			subsetRank = "subfamily";
			subsetValue = subfamily;
		} else if (family != null) {
			subsetRank = "family";
			subsetValue = family;
		}
		
		String queryString = null;
		if (subsetValue != null) {
			queryString = "select t.id from Taxon t left join t.acceptedNameUsage a  where t.#subsetRank = :subsetValue or a.#subsetRank = :subsetValue";
			queryString = queryString.replaceAll("#subsetRank", subsetRank);
			executionContext.put("index.taxon.subset.value", subsetValue);
		} else {
			queryString = "select t.id from MeasurementOrFact m join m.taxon t join m.annotations a where a.jobId = :jobId";
		}
		
		executionContext.put("index.taxon.query", queryString);
        executionContext.put("temporary.file.name", temporaryFile.getAbsolutePath());
        executionContext.put("split.file.name", splitFile.getAbsolutePath());
        executionContext.putLong("job.execution.id", chunkContext.getStepContext().getStepExecution().getJobExecutionId());
        return RepeatStatus.FINISHED;
    }
}
