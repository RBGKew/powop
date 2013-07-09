package org.emonocot.harvest.integration;

import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.Router;

public class CommentDestinationRouter {
	
	@Router
	public String getDestinationChannel(@Header("toAddress") String toAddress) {
		
		if(toAddress.startsWith("http://")) {
			return "scratchpad";
		} else {
			return "email";
		}
	}

}
