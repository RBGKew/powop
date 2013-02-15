package org.emonocot.harvest.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class CopyFileTasklet implements Tasklet {
	
	Logger logger = LoggerFactory.getLogger(CopyFileTasklet.class);

	private Resource from;
	
	private FileSystemResource to;
	
	private boolean append = false;

	public void setFrom(Resource from) {
		this.from = from;
	}

	public void setTo(FileSystemResource to) {
		this.to = to;
	}
	
	public void setAppend(boolean append) {
		this.append = append;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = from.getInputStream();
			outputStream = new FileOutputStream(to.getFile(),append);
			byte[] buffer = new byte[4096];
			int bytes_read;

			while ((bytes_read = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytes_read);
			}
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {

				}
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (IOException e) {
					;
				}
		}
		return RepeatStatus.FINISHED;
	}

}
