package de.plehr.Controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;


@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController  {

    @RequestMapping("/error")
    public String handleError(Model m,HttpServletRequest request) {
        m.addAttribute("errorcode", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        m.addAttribute("errormessage", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        return "error";
    }
}