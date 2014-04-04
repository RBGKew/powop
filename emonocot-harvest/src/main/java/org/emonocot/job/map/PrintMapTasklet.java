package org.emonocot.job.map;

import java.util.HashMap;
import java.util.Map;

import org.mapfish.print.MapPrinter;
import org.mapfish.print.utils.PJsonObject;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class PrintMapTasklet implements Tasklet {
	
	private MapPrinter mapPrinter;
	
	private Resource config;
	
	private FileSystemResource output;
	
	private String json;
	
	private Map<String,String> referer = new HashMap<String,String>();
	
	public void setConfig(Resource config) {
		this.config = config;
	}
	
	public void setJson(String json) {
		this.json = json;
	}
	
	public void setOutput(FileSystemResource output) {
		this.output = output;
	}	
	
	@Autowired
	public void setMapPrinter(MapPrinter mapPrinter) {
		this.mapPrinter = mapPrinter;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		mapPrinter.setYamlConfigFile(config.getFile());
		PJsonObject jsonSpec = MapPrinter.parseSpec(json);
		mapPrinter.print(jsonSpec, output.getOutputStream(), referer);
		return RepeatStatus.FINISHED;
	}

}
