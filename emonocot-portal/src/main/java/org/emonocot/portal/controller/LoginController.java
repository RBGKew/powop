package org.emonocot.portal.controller;

import javax.validation.Valid;

import org.emonocot.api.UserService;
import org.emonocot.model.auth.User;
import org.emonocot.portal.controller.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ben
 *
 */
@Controller
public class LoginController {
	
	private UserService userService;
	
    @Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

   @RequestMapping(value = "/login", method = RequestMethod.GET)
   public String setupForm(Model model) {
       model.addAttribute(new LoginForm());
       return "login";
   }
   
   @RequestMapping(value = "/activate/{nonce}", method = RequestMethod.GET) 
   public String verify(@PathVariable("nonce") String nonce,
		   @RequestParam String username,
		   Model model,
		   RedirectAttributes redirectAttributes) {
	   
	   if(userService.verifyNonce(username, nonce)) {
		   User user = userService.load(username);
		   user.setEnabled(true);
		   userService.update(user);
		   String[] codes = new String[] { "account.activated.successfully" };
		   Object[] args = new Object[] {  };
		   DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		   redirectAttributes.addFlashAttribute("info",message);
		   return "redirect:/login";
	   } else {
		   String[] codes = new String[] { "account.activation.failed" };
		   Object[] args = new Object[] {  };
		   DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		   redirectAttributes.addFlashAttribute("error",message);
		   return "redirect:/login";
	   }
	   
	   
   }
}
