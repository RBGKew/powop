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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;

import org.emonocot.api.CommentService;
import org.emonocot.api.UserService;
import org.emonocot.harvest.common.HtmlSanitizer;
import org.emonocot.model.Base;
import org.emonocot.model.BaseData;
import org.emonocot.model.Comment;
import org.emonocot.model.auth.User;
import org.emonocot.model.registry.Organisation;
import org.emonocot.model.registry.Resource;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.Payload;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.message.GenericMessage;


/**
 * @author jk00kg
 * 
 */
public class EmailServiceHelper {
	
	Logger logger = LoggerFactory.getLogger(EmailServiceHelper.class);

	Pattern pattern = Pattern
			.compile("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
	
	static Set<String> autoReplyPrecedenceValues = new HashSet<String>();
	
	static Set<String> autoReplySubjectValues = new HashSet<String>();
	
	static {
		autoReplyPrecedenceValues.add("auto_reply");
		autoReplyPrecedenceValues.add("bulk");
		autoReplyPrecedenceValues.add("junk");
		
		autoReplySubjectValues.add("Auto:");
		autoReplySubjectValues.add("Automatic reply");
		autoReplySubjectValues.add("Autosvar");
		autoReplySubjectValues.add("Automatisk svar");
		autoReplySubjectValues.add("Automatisch antwoord");
		autoReplySubjectValues.add("Abwesenheitsnotiz");
		autoReplySubjectValues.add("Risposta Non al computer");
		autoReplySubjectValues.add("Auto Response");
		autoReplySubjectValues.add("Respuesta automática");
		autoReplySubjectValues.add("Fuori sede");
		autoReplySubjectValues.add("Out of Office");
		autoReplySubjectValues.add("Frånvaro");
		autoReplySubjectValues.add("Réponse automatique");
	}

	private final String HEADER_TEMPLATE_NAME = "templateName";

	private String defaultTemplateName;
	
	private CommentService commentService;
	
	private UserService userService;

	/**
	 * A mapping between logical template/view names used in this class and
	 * resolvable template locations
	 */
	private Map<String, String> templates;

	private HtmlSanitizer htmlSanitizer;

	/**
	 * @param defaultTemplateName
	 *            the defaultTemplateName to set
	 */
	public void setDefaultTemplateName(String defaultTemplateName) {
		this.defaultTemplateName = defaultTemplateName;
	}

	/**
	 * @param templates
	 *            the templates to set
	 */
	public void setTemplates(Map<String, String> templates) {
		this.templates = templates;
	}

	@Autowired
	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setHtmlSanitizer(HtmlSanitizer htmlSanitizer) {
		this.htmlSanitizer = htmlSanitizer;
	}

	/**
	 * Create a comment replying to an incoming email
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public Message<Comment> createReply(Message<javax.mail.Message> message) throws Exception {
    	javax.mail.Message email = message.getPayload();
    	String subject = email.getSubject();
    	Matcher matcher = pattern.matcher(subject);
    	if(matcher.find()) {
    		Comment comment = new Comment();
    		comment.setIdentifier(UUID.randomUUID().toString());
    		comment.setCreated(new DateTime());
    		String identifier = matcher.group();
    		Comment inResponseTo = commentService.find(identifier,"aboutData");
    		
    		comment.setInResponseTo(inResponseTo);
    		
    		if(inResponseTo != null) {
    			comment.setCommentPage(inResponseTo.getCommentPage());
    			comment.setAboutData(inResponseTo.getAboutData());
    			if(comment.getAboutData() != null && comment.getAboutData() instanceof BaseData) {
    				
    			} else {
    				logger.debug("Response is about " + comment.getAboutData() + " not creating reply");
    				return null;
    			}
    			
    		} else {
    			logger.debug("Could not find comment with identifier " + identifier + " not creating reply");
    			return null;
    		}
    		comment.setComment(htmlSanitizer.sanitize(getText(email)));
    		for(Address address : email.getFrom()) {
				if(address instanceof InternetAddress) {
					InternetAddress internetAddress = (InternetAddress)address;
    			    User user = userService.find(internetAddress.getAddress());
    			    if(user != null) {
    				    comment.setUser(user);
    				    break;
    			    }
			    }
    		}
    		comment.setStatus(Comment.Status.PENDING);
    		String content = comment.getComment();
    		if(content != null && content.length() > 256) {
    			content = content.substring(0,255);
    		}
    		logger.debug("Recieved comment from " + comment.getUser() + " in response to " + comment.getInResponseTo() + " content " + content);
    		Map<String, Object> headers = new HashMap<String, Object>();
    		headers.putAll(message.getHeaders());
    		
    		return new GenericMessage<Comment>(comment, headers);
    	} else {
    		String content = getText(email);
    		if(content != null && content.length() > 256) {
    			content = content.substring(0,255);
    		}
    		logger.debug("Recieved comment from " + email.getFrom()[0].toString() + " with subject " + email.getSubject() + " and content " + content);
    		return null;
    	}	
    }
	
	/**
	 * http://stackoverflow.com/questions/1027395/detecting-outlook-autoreply-out-of-office-emails
	 * @param message
	 * @return
	 */
	@Filter
	public boolean filterOutOfOfficeReplies(@Payload javax.mail.Message message) {
		try {
		    if(message.getHeader("x-auto-response-suppress") != null) {
		    	logger.debug("Email contains header x-auto-response-suppress, filtering");
			    return false;
		    }
		    if(message.getHeader("x-autorespond") != null) {
		    	logger.debug("Email contains header x-autorespond, filtering");
			    return false;
		    }
		    if(message.getHeader("precedence") != null) {
		    	for(String header : message.getHeader("precedence")) {
		    		if(autoReplyPrecedenceValues.contains(header)) {
		    			logger.debug("Email contains header precedence = " + header +", filtering");
		    			return false;
		    		}
		    	}
		    }
		    if(message.getHeader("x-precedence") != null) {
		    	for(String header : message.getHeader("x-precedence")) {
		    		if(autoReplyPrecedenceValues.contains(header)) {
		    			logger.debug("Email contains header x-precedence = " + header +", filtering");
		    			return false;
		    		}
		    	}
		    }
		    if(message.getHeader("auto-submitted") != null) {
		    	for(String header : message.getHeader("auto-submitted")) {
		    		if(header.equals("auto-replied")) {
		    			logger.debug("Email contains auto-submitted = " + header +", filtering");
		    			return false;
		    		}
		    	}
		    }
		    if(message.getSubject() != null) {
		    	for(String subjectValue: this.autoReplySubjectValues) {
		    		if(message.getSubject().startsWith(subjectValue)) {
		    			logger.debug("Email contains subject " + message.getSubject() +", filtering");
		    			return false;
		    		}
		    	}
		    }
		
		    return true;
		} catch(MessagingException me) {
			logger.error("MessagingException thrown trying to filter message" + me.getLocalizedMessage());
			return false;
		}
	}

	private String getText(Part part) throws MessagingException, IOException {
		if (part.isMimeType("text/*")) {
			return (String) part.getContent();
		}

		if (part.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart multipart = (Multipart) part.getContent();
			String text = null;
			for (int i = 0; i < multipart.getCount(); i++) {
				Part bodyPart = multipart.getBodyPart(i);
				if (bodyPart.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bodyPart);
					continue;
				} else if (bodyPart.isMimeType("text/html")) {
					String string = getText(bodyPart);
					if (string != null)
						return string;
				} else {
					return getText(bodyPart);
				}
			}
			return text;
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			for (int i = 0; i < multipart.getCount(); i++) {
				String string = getText(multipart.getBodyPart(i));
				if (string != null)
					return string;
			}
		}

		return null;
	}

	/**
	 * Prepare a message for outgoing email
	 * 
	 * @param message
	 * @return
	 */
	public Message<Map> prepareMessage(Message<?> message) {
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.putAll(message.getHeaders());
		Object payload = message.getPayload();
		if (payload instanceof Comment) {
			model.put("comment", payload);
			// Decide which template
			Base about = ((Comment) payload).getAboutData();
			String templateName = null;
			if(((Comment) payload).getInResponseTo() != null) {
				templateName = "reply";
			} else if (about instanceof BaseData) {
				templateName = "comment";
			} else if (about instanceof Resource) {
				templateName = "resource";
			}
			if (templateName != null) {
				headers.put(HEADER_TEMPLATE_NAME, templates.get(templateName));
			} else {
				headers.put(HEADER_TEMPLATE_NAME,
						templates.get(defaultTemplateName));
			}
		}
		return new GenericMessage<Map>(model, headers);

	}
	
	@Filter
	public boolean preventSelfSending(@Header("toAddress") String toAddress, @Payload Comment comment) {
		if(comment.getAuthority() != null) {
			Organisation authority = comment.getAuthority();
			if(authority.getIdentifier().equals(toAddress)) {
				return false;
			}
			if(authority.getCommentsEmailedTo() != null && authority.getCommentsEmailedTo().equals(toAddress)) {
				return false;
			}
		}
		return true;
		
	} 
	
	@Router
	public String getDestinationChannel(@Header("toAddress") String toAddress) {
		
		if(toAddress.startsWith("http://")) {
			return "scratchpad";
		} else {
			return "email";
		}
	}
}
