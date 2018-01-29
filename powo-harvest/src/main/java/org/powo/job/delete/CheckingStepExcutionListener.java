package org.powo.job.delete;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class CheckingStepExcutionListener implements StepExecutionListener{

	private static Logger logger = LoggerFactory.getLogger(CheckingStepExcutionListener.class);
	
	@Override
	public ExitStatus afterStep(StepExecution step) {
		List<Throwable> exceptions = step.getFailureExceptions();
		
		if(exceptions != null && !exceptions.isEmpty()){
			ExitStatus exitStatus = new ExitStatus("DELETE FAILED");
			for(Throwable exception : exceptions){
				if(ResourceIsNotDeletableException.class.isInstance(exception.getCause())){
					String message = exception.getMessage();
					if(message != null){
						exitStatus.addExitDescription(message);
						logger.debug(exitStatus.toString());
						return exitStatus;
					}
			
				}else{
					exitStatus.addExitDescription("unknown failure - this resource could not be deleted");
					logger.debug(exitStatus.toString());
					return exitStatus;
				}
			}
		}
		logger.debug("READY FOR DELETE");
		return new ExitStatus("READY FOR DELETE");
	}

	@Override
	public void beforeStep(StepExecution begin) {

		
	}
	
}	
