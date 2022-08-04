package com.jcoding.zenithanalysis.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class ZenithErrorController implements ErrorController {

    public String handleError(HttpServletRequest httpServletRequest, Model model){
        Object status = httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null){
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("errorCode",statusCode);
        }
        return "error";
    }
}
