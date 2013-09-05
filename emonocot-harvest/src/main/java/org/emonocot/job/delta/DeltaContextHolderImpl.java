package org.emonocot.job.delta;

import java.io.File;

import org.springframework.core.io.Resource;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.directives.ConforDirectiveFileParser;
import au.org.ala.delta.directives.ConforDirectiveParserObserver;

public class DeltaContextHolderImpl implements DeltaContextHolder {

	private DeltaContext deltaContext;
	
	private Resource specsFile;
	
	public DeltaContext getDeltaContext() {
		return deltaContext;
	}

	public void setSpecsFile(Resource specsFile) {
		this.specsFile = specsFile;
	}

	public void init() throws Exception {
		deltaContext = new DeltaContext();
		File specs = specsFile.getFile();

		ConforDirectiveFileParser parser = ConforDirectiveFileParser.createInstance();
		ConforDirectiveParserObserver conforObserver = new ConforDirectiveParserObserver(deltaContext);
		parser.registerObserver(conforObserver);
		parser.parse(specs, deltaContext);
		
		conforObserver.finishedProcessing();
	}
}
