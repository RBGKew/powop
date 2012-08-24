/**
 * 
 */
package org.emonocot.job.sitemap;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.emonocot.model.marshall.xml.StaxEventItemWriter;
import org.emonocot.portal.model.Url;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.xstream.XStreamMarshaller;

/**
 * @author jk00kg
 *
 */
public class SitemapFilesListener implements JobExecutionListener, StepExecutionListener, ChunkListener {
	
	/**
	 * 
	 */
	private static final Logger logger = LoggerFactory.getLogger(SitemapFilesListener.class);

	/**
	 * The maximum size allowed by the sitemap protocol is 10MB
	 */
	private static final long MAX_SITEMAP_LENGTH = 9 * 1024 * 1024;

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
	private List<Url> sitemapNames = new ArrayList<Url>();
	
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
	 * 
	 */
	private XStreamMarshaller marshaller;

	/**
	 * @param portalBaseUrl the portalBaseUrl to set
	 */
	public final void setPortalBaseUrl(String portalBaseUrl) {
		this.portalBaseUrl = portalBaseUrl;
	}

	/**
	 * @return the sitemapNames
	 */
	public final List<Url> getSitemapNames() {
		return sitemapNames;
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

	/**
	 * @param marshaller the marshaller to set
	 */
	public final void setMarshaller(XStreamMarshaller marshaller) {
		this.marshaller = marshaller;
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
		logger.debug("After Step " + currentStep.getStepName());
		
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
			logger.debug("Creating a new file");
			
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

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// Nothing to do here
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		//This should be configured as a separate MethodInvokingTasklet
		// Get sitemap file/URL list
		logger.debug("After Job");
		Collection<File> files = FileUtils.listFiles(new File(sitemapSpoolDir), null, false);
		logger.debug(files.toString());
		
		// Make URLs
		List<Url> urls = new ArrayList<Url>();
		DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();
			for (File file : files) {
				try {
					Url u = new Url();
					u.setLastmod(dateTimeFormatter.print((ReadableInstant) null));
					u.setLoc(new URL(portalBaseUrl + "/" + file.getPath()));
					urls.add(u);
				} catch (MalformedURLException e) {
					logger.error("Unable create Url for sitemap", e);
				}
			}
			logger.debug(urls.size() + " urls in " + urls.toString());
			
		// init staxWriter
		staxWriter.setMarshaller(marshaller);
		Map<String, Class> aliases = new HashMap<String, Class>();
		aliases.put("sitemap", Url.class);
		try {
			marshaller.setAliases(aliases);
		} catch (ClassNotFoundException e) {
			logger.error("Error configuring the XML marchaller",e);
		}
		staxWriter.setRootTagName("sitemapindex");
		staxWriter.setResource((Resource) new FileSystemResource(sitemapSpoolDir + "/sitemapindex.xml"));
		staxWriter.open(jobExecution.getExecutionContext());
		
		//write
		try {
			staxWriter.write(urls);
		} catch (Exception e) {
			logger.error("Error configuring the XML marchaller",e);
		}
	}

}