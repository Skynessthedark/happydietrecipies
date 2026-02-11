package com.happydieting.dev.controller;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_PAGE = "error/error";

    @RequestMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        if (request.getDispatcherType() != DispatcherType.ERROR) {
            return ERROR_PAGE;
        }

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Throwable exception =
                (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        String path =
                (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        model.addAttribute("status", statusCode);
        model.addAttribute("path", path);

        if (exception != null) {
            model.addAttribute("message", exception.getMessage());
        }

        if (statusCode == null) {
            return ERROR_PAGE;
        }

        return switch (statusCode) {
            case 404 -> "error/404";
            case 403 -> "error/403";
            case 500 -> "error/500";
            default -> ERROR_PAGE;
        };
    }
}
