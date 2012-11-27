package org.emonocot.portal.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.emonocot.api.UserService;
import org.emonocot.model.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ben
 *
 */
@Controller
public class RegistrationController {

    /**
     *
     */
    private UserService service;

    /**
     * @param userService set the user service
     */
    @Autowired
    public final void setUserService(final UserService userService) {
        service = userService;
    }

    /**
     *
     * @return a model and view containing a registration form object
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public final ModelAndView setupForm() {
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
    public final String post(
            @Valid @ModelAttribute("registrationForm") final RegistrationForm form,
            final BindingResult result, final HttpSession session,
            final Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(form.getPassword());
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        service.createUser(user);
        String[] codes = new String[] {"registration.successful" };
        Object[] args = new Object[] {};
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/home";
    }
}
