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
