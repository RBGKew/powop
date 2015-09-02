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
