package org.emonocot.harvest.integration;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.emonocot.harvest.common.GetResourceClient;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class ScratchpadCommentService {
	
	private VelocityEngine velocityEngine;
	
	private GetResourceClient getResourceClient;
	
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
	public void setGetResourceClient(GetResourceClient getResourceClient) {
		this.getResourceClient = getResourceClient;
	}
	
	public void sendCommnent(String templateName, Map model, String toAddress, String subject) {
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, model);
		
	}

}
