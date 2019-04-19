package io.github.aquerr.secretchat.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserRestController implements Controller
{
//    @Autowired
//    private UserService userService;

//    @ResponseStatus()
//    @PostMapping(path = "/register", consumes = "application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public User register(@RequestBody final Map<String, Object> jsonNode, final Model model)
//    {
//        final String username = String.valueOf(jsonNode.get("username"));
//        final String password = String.valueOf(jsonNode.get("password"));
//        final String repeatedPassword = String.valueOf(jsonNode.get("repeatedPassword"));
//        final String emailAddress = String.valueOf(jsonNode.get("email"));
//
//        //TODO: Consider if this check is necessary. Having it only in the view should be fine.
//        if (!password.equals(repeatedPassword))
//        {
//            throw new HttpServerErrorException(HttpStatus.PRECONDITION_FAILED, "Passwords are not the same!");
//        }
//
//        this.userService.register(username, password, emailAddress);
//
////        final Map<String, Object> returnMap = new HashMap<>();
////        returnMap.put("username", "Test");
////        return returnMap;
//
////        model.addAttribute("username", "Test");
////        return model;
//        return new User(1, "Test", 1, "Test", "Test");
////        return "register";
//    }

    @Override
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception
    {
        request.getSession().getAttribute("username");
        response.sendError(500, "BOOM");
        return null;
    }
}
