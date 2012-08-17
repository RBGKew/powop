/**
 * 
 */
package org.emonocot.harvest.job.sitemap;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.emonocot.model.marshall.xml.StaxEventItemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * @author jk00kg
 *
 */
public class SitemapFilesListener implements StepExecutionListener, ChunkListener {
	
	/**
	 * 
	 */
	private static final Logger logger = LoggerFactory.getLogger(SitemapFilesListener.class);

	/**
	 * The maximum size allowed by the sitemap protocol is 10MB
	 */
	private static final long MAX_SITEMAP_LENGTH = 9 * 1024;

	/**
	 * The maximum number of url elements in a single sitemap file
	 */
	private static final int MAX_URL_COUNT = 9000;
	
	/**
	 * 
	 */
	private StepExecution currentStep;
	
	/**
	 * 
	 */
	private List<String> sitemapNames = new ArrayList<String>();
	
	/**
	 * 
	 */
	private String portalBaseUrl;
	
	/**
	 * 
	 */
	private String sitemapSpoolDir;
	
	/**
	 * 
	 */
	private StaxEventItemWriter staxWriter;

	/**
	 * Initialized in beforeStep
	 */
	private int fileCount;

	/**
	 * Initialized in beforeStep
	 */
	private FileSystemResource currentFile;

	/**
	 * 
	 */
	private int chunkOfFile = 0;

	/**
	 * 
	 */
	private int commitSize = 1000;

	/**
	 * @param portalBaseUrl the portalBaseUrl to set
	 */
	public final void setPortalBaseUrl(String portalBaseUrl) {
		this.portalBaseUrl = portalBaseUrl;
	}

	/**
	 * @param sitemapSpoolDir the sitemapSpoolDir to set
	 */
	public final void setSitemapSpoolDir(String sitemapSpoolDir) {
		this.sitemapSpoolDir = sitemapSpoolDir;
	}

	/**
	 * @param staxWriter the staxWriter to set
	 */
	public final void setStaxWriter(StaxEventItemWriter staxWriter) {
		this.staxWriter = staxWriter;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.StepExecutionListener#beforeStep(org.springframework.batch.core.StepExecution)
	 */
	public void beforeStep(StepExecution stepExecution) {
		currentStep = stepExecution;
		fileCount = 0;
		currentFile = new FileSystemResource(sitemapSpoolDir + "/"+ currentStep.getStepName()  + fileCount + ".xml");
		logger.debug("currentStep set to: " + currentStep.getStepName());
		
		//Set here because it can change at the end of a chunk
		staxWriter.setResource((Resource) currentFile);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.StepExecutionListener#afterStep(org.springframework.batch.core.StepExecution)
	 */
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.error("After Step " + currentStep.getStepName());
		//Add filename to collection
		sitemapNames.add(currentFile.getFilename());
		
		currentStep = null;
		currentFile = null;
		chunkOfFile = 0;
		commitSize = 0;
		//reset counts to nulls to support beforeStep()?
		return stepExecution.getExitStatus();
	}

	public void beforeChunk() {
		//Check sizes (MB & count) & if over limit
		if (FileUtils.sizeOf(currentFile.getFile()) >= MAX_SITEMAP_LENGTH || (chunkOfFile * commitSize) >= MAX_URL_COUNT){
			logger.error("Creating a new file");
			
			//close & open writer with new name
			staxWriter.close();
			sitemapNames.add(currentFile.getFilename());
			currentFile = new FileSystemResource(sitemapSpoolDir + "/"+ currentStep.getStepName()  + ++fileCount + ".xml");
			logger.error("Open:" + currentFile.isOpen());
			logger.error("Writable:" + currentFile.isWritable());
			staxWriter.setResource((Resource) currentFile);
			staxWriter.open(currentStep.getExecutionContext());

			chunkOfFile = 0;
		}
	}

	public void afterChunk() {
		logger.debug("End Chunk " + currentStep.getCommitCount());
		chunkOfFile++;

		if (currentStep.getCommitCount() == 1) {
			commitSize = currentStep.getReadCount() + currentStep.getReadSkipCount();
			logger.debug("Set commitSize to " + commitSize);
		}
	}

}
