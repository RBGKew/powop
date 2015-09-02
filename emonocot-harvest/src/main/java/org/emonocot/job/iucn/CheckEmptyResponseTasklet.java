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


import java.io.BufferedInputStream;
import java.io.InputStreamReader;

import org.springframework.batch.core.ExitStatus;
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
public class CheckEmptyResponseTasklet implements
Tasklet {

	private Resource inputFile;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputFile.getInputStream());
		char[] buffer = new char[2];
		InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream);
		int numChars = -1;
		if((numChars = inputStreamReader.read(buffer)) == 2) {
			String string = new String(buffer);
			if(string.equals("[]")) {
				contribution.setExitStatus(new ExitStatus("EMPTY_RESPONSE").addExitDescription("The webservice returned an empty list of taxa"));
			}
		} else {
			contribution.setExitStatus(ExitStatus.FAILED.addExitDescription("Unable to read the webservice response"));
		}

		inputStreamReader.close();

		return RepeatStatus.FINISHED;
	}



	/**
	 * @param inputFile the inputFile to set
	 */
	public void setInputFile(Resource inputFile) {
		this.inputFile = inputFile;
	}

}
