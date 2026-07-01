package com.foro.app.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("mensaje", ex.getMessage());
        mav.addObject("status", 404);
        return mav;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Object handleUnauthorized(UnauthorizedException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/login";
        }
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("mensaje", ex.getMessage());
        mav.addObject("status", 401);
        return mav;
    }

    @ExceptionHandler(SuspendedUserException.class)
    public Object handleSuspended(SuspendedUserException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("Referer");
        if (referer != null) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:" + referer;
        }
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("mensaje", ex.getMessage());
        mav.addObject("status", 403);
        return mav;
    }

    @ExceptionHandler(BadRequestException.class)
    public Object handleBadRequest(BadRequestException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("Referer");
        if ("POST".equalsIgnoreCase(request.getMethod()) && referer != null) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:" + referer;
        }
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("mensaje", ex.getMessage());
        mav.addObject("status", 400);
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public Object handleGeneralException(Exception ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ex.printStackTrace();
        String referer = request.getHeader("Referer");
        if ("POST".equalsIgnoreCase(request.getMethod()) && referer != null) {
            redirectAttributes.addFlashAttribute("error", "Error: " + ex.getMessage());
            return "redirect:" + referer;
        }
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("mensaje", "Ocurrió un error en el servidor: " + ex.getMessage());
        mav.addObject("status", 500);
        return mav;
    }
}

