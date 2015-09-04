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
package org.emonocot.harvest.integration;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.emonocot.api.CommentService;
import org.emonocot.harvest.common.GetResourceClient;
import org.emonocot.model.Comment;
import org.emonocot.model.Description;
import org.emonocot.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.ui.velocity.VelocityEngineUtils;


public class ScratchpadCommentService {

	private Logger logger = LoggerFactory.getLogger(ScratchpadCommentService.class);

	private VelocityEngine velocityEngine;

	private GetResourceClient getResourceClient;

	private CommentService commentService;

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void setGetResourceClient(GetResourceClient getResourceClient) {
		this.getResourceClient = getResourceClient;
	}

	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

	public void sendComment(String templateName, Map<String, Object> model, String toAddress, String subject) {

		String message = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, "UTF-8", model);
		Map<String,String> params = new HashMap<String,String>();
		Comment comment = (Comment)model.get("comment");

		if(comment.getInResponseTo() != null && comment.getInResponseTo().getAlternativeIdentifiers().containsKey(toAddress)) {
			params.put("inreplyto", comment.getInResponseTo().getAlternativeIdentifiers().get(toAddress));
		} else {
			if(comment.getAboutData() != null) {
				if(comment.getAboutData() instanceof Description) {
					Description description = (Description) comment.getAboutData();
					params.put("uri",description.getSource());
				} else if(comment.getAboutData() instanceof Reference) {
					Reference reference = (Reference) comment.getAboutData();
					params.put("uri",reference.getIdentifier());
				}
			}
		}

		if(params.containsKey("uri") || params.containsKey("inreplyto")) {
			params.put("username", comment.getUser().getAccountName());
			params.put("email", comment.getUser().getIdentifier());
			params.put("comment", message);
			params.put("title", subject);
			Map<String,String> responseHeaders = new HashMap<String,String>();
			StringBuffer response = new StringBuffer();

			ExitStatus exitStatus = getResourceClient.postBody(toAddress, params, response, responseHeaders);
			if(exitStatus.equals(ExitStatus.COMPLETED)) {
				if(responseHeaders.containsKey("Location")) {
					logger.debug("Comment " + comment +" inserted, alternative identifier is " + responseHeaders.get("Location"));
					commentService.updateAlternativeIdentifiers(comment.getIdentifier(), toAddress, responseHeaders.get("Location"));
				} else {
					logger.debug("Comment " + comment +" inserted, no alternative identifier returned");
				}
			} else {
				logger.error("Unable to insert comment into Scratchpad " + toAddress + ":\n" + response.toString());
			}
		} else {
			logger.debug("No object uri or parent comment identifier found, not inserting comment");
		}
	}

}
