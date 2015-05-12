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
package org.emonocot.job.gbif;

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

public class HasMoreRecords implements StepExecutionListener {
	
	private Logger logger = LoggerFactory.getLogger(HasMoreRecords.class);
	
	private StepExecution stepExecution;
	
	private Unmarshaller unmarshaller;
	
	public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }
	
	public ExitStatus execute(String temporaryFileName) throws Exception {
        try {
            StaxEventItemReader<GbifResponse> staxEventItemReader = new StaxEventItemReader<GbifResponse>();
            staxEventItemReader.setFragmentRootElementName(
                    "{http://portal.gbif.org/ws/response/gbif}gbifResponse");
            staxEventItemReader.setUnmarshaller(unmarshaller);
            staxEventItemReader.setResource(new FileSystemResource(
                    temporaryFileName));

            staxEventItemReader.afterPropertiesSet();
            staxEventItemReader.open(stepExecution.getExecutionContext());

            GbifResponse gbifResponse = staxEventItemReader.read();
            staxEventItemReader.close();
            if(gbifResponse.getExceptionReport() != null) {
            	return new ExitStatus("SERVER_ERROR").addExitDescription(gbifResponse.getExceptionReport());
            } else if (gbifResponse.getHeader() == null || gbifResponse.getHeader().getSummary() == null) {
                logger.info("Header Not Found");                
                return new ExitStatus("NO_MORE_RECORDS");
            } else {
                if(gbifResponse.getHeader().getSummary().getNext() != null){
					stepExecution.getJobExecution().getExecutionContext().remove("startindex");
					stepExecution.getJobExecution().getExecutionContext().put("startindex", gbifResponse.getHeader().getSummary().getNext());
                    return new ExitStatus("HAS_MORE_RECORDS");
				} else {
					return new ExitStatus("NO_MORE_RECORDS");
				}
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
	
	public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
	
}
