package org.emonocot.portal.controller;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.CommentService;
import org.emonocot.model.Comment;
import org.emonocot.pager.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {
	
	private CommentService commentService;

	@Autowired
	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@RequestMapping(method = RequestMethod.GET,produces = "text/html")
	public String index(Model uiModel) {
		Map<String, String> selectedFacets = new HashMap<String, String>();		
		selectedFacets.put("base.class_s", "org.emonocot.model.Comment");
		selectedFacets.put("comment.status_t", "SENT");
		Page<Comment> result = commentService.search(null, null, 5, 0, null, null, selectedFacets, "comment.created_dt_desc", null);
		uiModel.addAttribute("comments", result);
		return "index";
	}

}
