package org.emonocot.job.delete;

import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class CheckingStepExcutionListener implements StepExecutionListener{

	@Override
	public ExitStatus afterStep(StepExecution step) {
		List<Throwable> exceptions = step.getFailureExceptions();
		ExitStatus exitStatus = new ExitStatus("READY FOR DELETE");
		if(exceptions != null && !exceptions.isEmpty()){
			for(Throwable exception : exceptions){
				if(ResourceIsNotDeletableException.class.isInstance(exception.getCause())){
					String message = exception.getMessage();
					if(message != null){
						exitStatus.replaceExitCode("DELETE FAILED");
						exitStatus.addExitDescription(message);
						return exitStatus;
					}
			
				}else{
					exitStatus.replaceExitCode("DELETE FAILED");
					exitStatus.addExitDescription("unknown failure - this resource could not be deleted");
					return exitStatus;
				}
			}
		}
		return exitStatus;
	}

	@Override
	public void beforeStep(StepExecution begin) {

		
	}
	
}	
