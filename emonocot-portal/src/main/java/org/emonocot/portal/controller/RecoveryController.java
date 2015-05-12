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
package org.emonocot.portal.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.emonocot.api.UserService;
import org.emonocot.portal.controller.form.RecoveryForm;
import org.emonocot.portal.controller.form.ResetForm;
import org.emonocot.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping(value = "/recovery")
public class RecoveryController {
	
	private static Logger logger = LoggerFactory.getLogger(RecoveryController.class);
	
	private UserService userService;
	
	private EmailService emailService;
	
	private String baseUrl;
	
	@Autowired
	public void setUserController(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
   
   @RequestMapping(method = RequestMethod.GET)
   public String show(Model model) {
	   model.addAttribute(new RecoveryForm());
	   return "recovery/show";
   }
   
   @RequestMapping(method = RequestMethod.POST)
   public String reset(@Valid @ModelAttribute RecoveryForm recoveryForm,
		   BindingResult result, RedirectAttributes redirectAttributes) {
	   if (result.hasErrors()) {
           return "recovery/show";
       }
	   if(null == userService.find(recoveryForm.getUsername())) {
		   String[] codes = new String[] { "problem.resetting.password" };
		   Object[] args = new Object[] {  };
		   DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		   redirectAttributes.addFlashAttribute("error", message);
		   return "redirect:/recovery";
	   }
	   
	   String nonce = userService.createNonce(recoveryForm.getUsername());
	   
	   Map<String,String> model = new HashMap<String,String>();
	   model.put("nonce", nonce);
	   model.put("baseUrl", baseUrl);
	   emailService.sendEmail("org/emonocot/portal/controller/ResetPasswordRequest.vm", model, recoveryForm.getUsername(), "Password Reset Request");
	   String[] codes = new String[] { "reset.email.sent" };
	   Object[] args = new Object[] {  };
	   DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
	   redirectAttributes.addFlashAttribute("info", message);

       return "redirect:/recovery";
   }
   
   @RequestMapping(value = "/{nonce}", method = RequestMethod.GET) 
   public String verifyPage(@PathVariable("nonce") String nonce, Model model) {
	   ResetForm resetForm = new ResetForm();
	   model.addAttribute(resetForm);
	   return "recovery/verify";
   }
   
   @RequestMapping(value = "/{nonce}", method = RequestMethod.POST) 
   public String verify(@PathVariable("nonce") String nonce,
		   @Valid @ModelAttribute ResetForm resetForm,
		   Model model,
		   BindingResult result, RedirectAttributes redirectAttributes) {
	   if (result.hasErrors()) {
           return "recovery/verify";
       }
	   if(userService.verifyNonce(resetForm.getUsername(), nonce)) {
		   userService.changePasswordForUser(resetForm.getUsername(),resetForm.getPassword());
		   String[] codes = new String[] { "password.recovery.successful" };
		   Object[] args = new Object[] {  };
		   DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		   redirectAttributes.addFlashAttribute("info",message);
		   return "redirect:/login";
	   } else {
		   String[] codes = new String[] { "password.recovery.failed" };
		   Object[] args = new Object[] {  };
		   DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		   model.addAttribute("error",message);
		   return "recovery/verify";
	   }
	   
	   
   }

}
