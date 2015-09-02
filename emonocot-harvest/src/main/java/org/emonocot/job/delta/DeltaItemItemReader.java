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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;

import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.core.io.Resource;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.directives.ItemDescriptions;
import au.org.ala.delta.directives.args.DirectiveArguments;
import au.org.ala.delta.model.Item;

public class DeltaItemItemReader extends AbstractItemCountingItemStreamItemReader<Item> {

	private DeltaContext deltaContext = null;

	private Resource itemsFile = null;

	private DataInputStream dataInputStream = null;

	private BufferedReader bufferedReader = null;

	private ItemDescriptions itemsParser = null;

	public void setDeltaContextHolder(DeltaContextHolder deltaContextHolder) {
		assert deltaContextHolder != null;
		this.deltaContext = deltaContextHolder.getDeltaContext();
	}

	public void setItemsFile(Resource itemsFile) {
		this.itemsFile = itemsFile;
	}

	public DeltaItemItemReader() {
		setName("DeltaItemItemReader");
	}

	private String readDirective() throws Exception {
		StringBuffer itemBuffer = null;
		String line;
		boolean moreLines = true;
		while (moreLines) {
			bufferedReader.mark(250);
			line = bufferedReader.readLine();
			if(line == null) {
				moreLines = false;
			} else {
				line = line.trim();
				if (line.isEmpty()) {
					break;
					// item ends or ignore
				} else if (line.startsWith("*")) {
					// other directive
				} else if (line.startsWith("#")) {
					if(itemBuffer != null) {
						String finishedItem = itemBuffer.toString();
						if(!finishedItem.trim().isEmpty()) {
							bufferedReader.reset();
							// return the current buffer
							return itemBuffer.toString();
						}
					}

					itemBuffer = new StringBuffer();
					itemBuffer.append(line);

				} else {
					if (itemBuffer != null) {
						itemBuffer.append("\n" + line);
					}
				}
			}
		}
		if(itemBuffer != null) {
			return itemBuffer.toString();
		} else if(moreLines) {
			return "";
		} else {
			return null;
		}
	}

	@Override
	protected Item doRead() throws Exception {
		boolean moreItems = true;
		String item = null;
		while(moreItems) {
			item = readDirective();
			if(item == null) {
				moreItems = false;
			} else if(!item.isEmpty()) {
				break;
			}
		}


		if(item == null || item.isEmpty()) {
			return null;
		} else {
			DirectiveArguments directiveArguments = new DirectiveArguments();
			directiveArguments.addTextArgument(item); // Add the item
			itemsParser.process(deltaContext, directiveArguments);
			return deltaContext.getItem(1);
		}
	}

	@Override
	protected void doOpen() throws Exception {
		dataInputStream = new DataInputStream(itemsFile.getInputStream());
		bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
		itemsParser = new ItemDescriptions();
	}

	@Override
	protected void doClose() throws Exception {
		dataInputStream.close();
	}
}
