//package io.github.aquerr.secretchat.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.error.ErrorAttributes;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.context.request.WebRequest;
//
//import java.util.Map;
//
//@Controller
//public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController
//{
//    private ErrorAttributes errorAttributes;
//
////    @GetMapping("/error")
////    public String error(String stacktrace, Model model)
////    {
////        model.addAttribute("stacktrace", stacktrace);
////        return "error";
////    }
//
//    //TODO: This method should be run if spring will redirect user to /error page.
////    @ExceptionHandler(Exception.class)
////    public ResponseEntity<?> handleError(WebRequest request, Throwable ex)
////    {
////        ModelAndView modelAndView = new ModelAndView();
////        modelAndView.addObject("exception", ex);
////        modelAndView.addObject("url", request.getContextPath());
////        modelAndView.setViewName("errorpage");
////        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
////    }
//
//    @Autowired
//    public ErrorController(ErrorAttributes errorAttributes)
//    {
//        this.errorAttributes = errorAttributes;
//    }
//
//    @RequestMapping(path = "/error")
//    public String test(WebRequest request)
//    {
//        //TODO: If status 404 then display information about it on the errorpage.
//        Map<String, Object> map = errorAttributes.getErrorAttributes(request, true);
//        if (map.containsKey("status") && map.get("status").equals("404"))
//        {
//            return "errorpage";
//        }
//        return null;
////        return request.getSessionMutex();
//    }
//
//    @Override
//    public String getErrorPath()
//    {
//        return "/error";
//    }
//}
