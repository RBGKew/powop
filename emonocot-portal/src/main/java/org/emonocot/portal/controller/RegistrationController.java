package org.emonocot.portal.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.emonocot.api.UserService;
import org.emonocot.model.auth.User;
import org.emonocot.portal.controller.form.RegistrationForm;
import org.emonocot.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ben
 *
 */
@Controller
public class RegistrationController {

    private UserService service;
    
	private EmailService emailService;
	
	private String baseUrl;

    @Autowired
    public void setUserService(UserService userService) {
        this.service = userService;
    }
    
    @Autowired
    public void setEmailService(EmailService emailService) {
    	this.emailService = emailService;
    }
    
    public void setBaseUrl(String baseUrl) {
    	this.baseUrl = baseUrl;
    }

    /**
     *
     * @return a model and view containing a registration form object
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView setupForm() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject(new RegistrationForm());
        return modelAndView;
    }

    /**
     *
     * @param form
     *            Set the registration form
     * @param result
     *            Set the binding results
     * @param session
     *            Set the http session
     * @param model
     *            Set the model
     * @return the view name
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String post(
            @Valid @ModelAttribute("registrationForm") RegistrationForm form,
            BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }

        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(form.getPassword());
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);

        service.createUser(user);
        String nonce = service.createNonce(user.getUsername());
        
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("user", user);
        model.put("nonce", nonce);
        model.put("baseUrl", baseUrl);
        emailService.sendEmail("org/emonocot/portal/controller/ActivateAccountRequest.vm", model, user.getUsername(), "Welcome! Your account requires activation");
        
        String[] codes = new String[] {"registration.successful" };
        Object[] args = new Object[] {};
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
        redirectAttributes.addFlashAttribute("info", message);
        return "redirect:/login";
    }
}
