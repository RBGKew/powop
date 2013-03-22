/**
 * 
 */
package org.emonocot.integration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import org.emonocot.api.CommentService;
import org.emonocot.api.UserService;
import org.emonocot.model.Base;
import org.emonocot.model.BaseData;
import org.emonocot.model.Comment;
import org.emonocot.model.auth.User;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.message.GenericMessage;


/**
 * @author jk00kg
 * 
 */
public class EmailServiceHelper {
	
	Logger logger = LoggerFactory.getLogger(EmailServiceHelper.class);

	Pattern pattern = Pattern
			.compile("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");

	private final String HEADER_TEMPLATE_NAME = "templateName";

	private String defaultTemplateName;
	
	private CommentService commentService;
	
	private UserService userService;

	/**
	 * A mapping between logical template/view names used in this class and
	 * resolvable template locations
	 */
	private Map<String, String> templates;

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
    		}
    		comment.setComment(getText((Part)email.getContent()));
    		for(Address address : email.getFrom()) {
    			User user = userService.find(address.toString());
    			if(user != null) {
    				comment.setUser(user);
    				break;
    			}
    		}
    		comment.setStatus(Comment.Status.RECIEVED);
    		String content = comment.getComment();
    		if(content != null && content.length() > 256) {
    			content = content.substring(0,255);
    		}
    		logger.debug("Recieved comment from " + comment.getUser() + " in response to " + comment.getInResponseTo() + " content " + content);
    		Map<String, Object> headers = new HashMap<String, Object>();
    		headers.putAll(message.getHeaders());
    		
    		return new GenericMessage<Comment>(comment, headers);
    	} else {
    		String content = getText((Part)email.getContent());
    		if(content != null && content.length() > 256) {
    			content = content.substring(0,255);
    		}
    		logger.debug("Recieved comment from " + email.getFrom()[0].toString() + " with subject " + email.getSubject() + " and content " + content);
    		return null;
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
			if (about instanceof BaseData) {
				templateName = "comment";
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

}
