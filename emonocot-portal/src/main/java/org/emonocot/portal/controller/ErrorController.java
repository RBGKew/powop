package org.emonocot.portal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author ben
 *
 */
@Controller
public class ErrorController {

    /**
     * @return the name of the index view
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public final String notFound() {
        return "404";
    }

    /**
     * @return the name of the index view
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping(value = "/500", method = RequestMethod.GET)
    public final String exception() {
        return "500";
    }

    /**
     * @return the name of the index view
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @RequestMapping(value = "/405", method = RequestMethod.GET)
    public final String notSupported() {
        return "405";
    }
}
