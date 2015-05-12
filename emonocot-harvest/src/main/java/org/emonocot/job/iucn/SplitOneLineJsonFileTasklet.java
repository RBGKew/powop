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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.regex.Pattern;

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
public class SplitOneLineJsonFileTasklet implements
		Tasklet {
	
	private Resource inputFile;
	
	private Resource outputFile;
	
	

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputFile.getInputStream());	
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile.getFile()));
		Scanner scanner = new Scanner(bufferedInputStream);
		scanner.useDelimiter(Pattern.compile("\\}\\,\\{"));
		
		while (scanner.hasNext()) {
			String string = scanner.next();
			
			if (!string.isEmpty()) {
				string = string.trim();
				if (string.startsWith("[{")) {
					string = string.substring(1);
				}
				if (!string.startsWith("{")) {
					string = "{" + string;
				}
				if (string.endsWith("}]")) {
					string = string.substring(0,string.length() - 1);
				}
				if (!string.endsWith("}")) {
					string = string + "}";
				}
				writer.write(string + "\n");
			}
		}
		bufferedInputStream.close();
		writer.close();
		return RepeatStatus.FINISHED;
	}



	/**
	 * @param inputFile the inputFile to set
	 */
	public void setInputFile(Resource inputFile) {
		this.inputFile = inputFile;
	}



	/**
	 * @param outputFile the outputFile to set
	 */
	public void setOutputFile(Resource outputFile) {
		this.outputFile = outputFile;
	}

}
