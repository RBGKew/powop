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
package org.emonocot.job.key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.tdwg.ubif.TechnicalMetadata;

public class DetermineKeyGenerator implements StepExecutionListener {

	private Logger logger = LoggerFactory.getLogger(DetermineKeyGenerator.class);

	private Unmarshaller unmarshaller;

	private StepExecution stepExecution;

	public void setUnmarshaller(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	public ExitStatus execute(String temporaryFileName) throws Exception {
		try {
			StaxEventItemReader<TechnicalMetadata> staxEventItemReader = new StaxEventItemReader<TechnicalMetadata>();
			staxEventItemReader.setFragmentRootElementName(
					"{http://rs.tdwg.org/UBIF/2006/}TechnicalMetadata");
			staxEventItemReader.setUnmarshaller(unmarshaller);
			staxEventItemReader.setResource(new FileSystemResource(
					temporaryFileName));

			staxEventItemReader.afterPropertiesSet();
			staxEventItemReader.open(stepExecution.getExecutionContext());

			TechnicalMetadata  technicalMetadata = staxEventItemReader.read();
			staxEventItemReader.close();
			if(technicalMetadata != null && technicalMetadata.getGenerator() != null && technicalMetadata.getGenerator().getName() != null) {
				switch(technicalMetadata.getGenerator().getName()) {
				case "Xper2":
					return new ExitStatus("XPER_2");
				case "Lucid3 Builder":
					return new ExitStatus("LUCID_3_BUILDER");
				default:
					return new ExitStatus("FAILED").addExitDescription("Unknown key generator " + technicalMetadata.getGenerator().getName());
				}
			} else {
				return new ExitStatus("FAILED").addExitDescription("No information on the key generator in this file");
			}

		} catch (UnexpectedInputException e) {
			logger.error(e.getMessage());
			for (StackTraceElement ste : e.getStackTrace()) {
				logger.error(ste.toString());
			}
			return ExitStatus.FAILED;
		} catch (ParseException e) {
			logger.error(e.getMessage());
			for (StackTraceElement ste : e.getStackTrace()) {
				logger.error(ste.toString());
			}
			return ExitStatus.FAILED;
		} catch (Exception e) {
			logger.error(e.getMessage());
			for (StackTraceElement ste : e.getStackTrace()) {
				logger.error(ste.toString());
			}
			return ExitStatus.FAILED;
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
