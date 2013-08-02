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
