/**
 * 
 */
package org.emonocot.job.sitemap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.emonocot.model.marshall.xml.StaxEventItemWriter;
import org.joda.time.ReadableInstant;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * @author jk00kg
 *
 */
public class SitemapFilesListener implements StepExecutionListener, ChunkListener {
	
	private static Logger logger = LoggerFactory.getLogger(SitemapFilesListener.class);

	/**
	 * The maximum size allowed by the sitemap protocol is 10MB
	 */
	private static long MAX_SITEMAP_LENGTH = 9 * 1024 * 1024;

	/**
	 * The maximum number of url elements in a single sitemap file
	 */
	private static int MAX_URL_COUNT = 9000;

	private StepExecution currentStep;
	
	private List<Url> sitemapNames = new ArrayList<Url>();
	
	private String portalBaseUrl;
	
	private String sitemapSpoolDir;

	private StaxEventItemWriter staxWriter;

	/**
	 * Initialized in beforeStep
	 */
	private int fileCount;

	/**
	 * Initialized in beforeStep
	 */
	private FileSystemResource currentFile;

	private int chunkOfFile = 0;

	private int commitSize = 1000;

	/**
	 * Used to hold a set of URLs written as the last step of the job
	 */
	private ExecutionContext jobExContext;

	private String sitemapDir;

	/**
	 * @param portalBaseUrl the portalBaseUrl to set
	 */
	public void setPortalBaseUrl(String portalBaseUrl) {
		this.portalBaseUrl = portalBaseUrl;
	}
	
	public void setSitemapDir(String sitemapDir) {
		this.sitemapDir = sitemapDir;
	}

	/**
	 * @return the sitemapNames
	 */
	public List<Url> getSitemapNames() {
		return sitemapNames;
	}

	/**
	 * @param sitemapSpoolDir the sitemapSpoolDir to set
	 */
	public void setSitemapSpoolDir(String sitemapSpoolDir) {
		this.sitemapSpoolDir = sitemapSpoolDir;
	}

	/**
	 * @param staxWriter the staxWriter to set
	 */
	public void setStaxWriter(StaxEventItemWriter staxWriter) {
		this.staxWriter = staxWriter;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.StepExecutionListener#beforeStep(org.springframework.batch.core.StepExecution)
	 */
	public void beforeStep(StepExecution stepExecution) {
		currentStep = stepExecution;
		logger.debug("currentStep set to: " + currentStep.getStepName());
		if (jobExContext == null){
			jobExContext = currentStep.getJobExecution().getExecutionContext();
			jobExContext.put("sitemaps.url", sitemapNames);
		}
		fileCount = 0;
		currentFile = new FileSystemResource(sitemapSpoolDir + "/"+ currentStep.getStepName()  + fileCount + ".xml");
		
		//Set here because it can change at the end of a chunk
		staxWriter.setResource((Resource) currentFile);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.StepExecutionListener#afterStep(org.springframework.batch.core.StepExecution)
	 */
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.debug("After Step " + currentStep.getStepName());

		try {
			Url u = new Url();
			u.setLastmod(ISODateTimeFormat.dateTime().print((ReadableInstant) null));
			u.setLoc(new URL(portalBaseUrl +"/" + sitemapDir + "/" + currentFile.getFilename()));
			sitemapNames.add(u);
		} catch (MalformedURLException e) {
			logger.error("Unable create Url for sitemap", e);
		}
		
		//reset counts to nulls to support beforeStep()
		currentStep = null;
		currentFile = null;
		chunkOfFile = 0;
		commitSize = 0;

		return stepExecution.getExitStatus();
	}

	public void beforeChunk() {
		//Check sizes (MB & count) & if over limit
		if (FileUtils.sizeOf(currentFile.getFile()) >= MAX_SITEMAP_LENGTH || (chunkOfFile * commitSize) >= MAX_URL_COUNT){
			logger.debug("Creating a new file");
			try {
				Url u = new Url();
				u.setLastmod(ISODateTimeFormat.dateTime().print((ReadableInstant) null));
				u.setLoc(new URL(portalBaseUrl + "/sitemap/" + currentFile.getFilename()));
				sitemapNames.add(u);
			} catch (MalformedURLException e) {
				logger.error("Unable create Url for sitemap", e);
			}
			//close & open writer with new name
			staxWriter.close();
			currentFile = new FileSystemResource(sitemapSpoolDir + "/"+ currentStep.getStepName()  + ++fileCount + ".xml");
			logger.debug("Open:" + currentFile.isOpen());
			logger.debug("Writable:" + currentFile.isWritable());
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
