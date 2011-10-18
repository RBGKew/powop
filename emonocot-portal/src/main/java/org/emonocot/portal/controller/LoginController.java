package org.emonocot.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ben
 *
 */
@Controller
public class LoginController {

   /**
    *
    * @return a model and view containing a login form object
    */
   @RequestMapping(value = "/login", method = RequestMethod.GET)
   public final ModelAndView setupForm() {
       ModelAndView modelAndView = new ModelAndView("login");
       modelAndView.addObject(new LoginForm());
       return modelAndView;
   }

}
