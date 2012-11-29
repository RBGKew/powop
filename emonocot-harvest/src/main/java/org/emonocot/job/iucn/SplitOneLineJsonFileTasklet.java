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
				if (!string.startsWith("{")) {
					string = "{" + string;
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
