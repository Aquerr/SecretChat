package io.github.aquerr.secretchat.controllers;

import io.github.aquerr.secretchat.models.User;
import io.github.aquerr.secretchat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
@Controller
public class UserController
{
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @GetMapping("/register")
    public String register()
    {
        return "register";
    }

    @ResponseBody
    @PostMapping(path = "/register", consumes = "application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User register(@RequestBody final Map<String, Object> jsonNode, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        final String username = String.valueOf(jsonNode.get("username"));
        final String password = String.valueOf(jsonNode.get("password"));
        final String repeatedPassword = String.valueOf(jsonNode.get("repeatedPassword"));
        final String emailAddress = String.valueOf(jsonNode.get("email"));

        if (username.equals("") || password.equals("") || repeatedPassword.equals("") || emailAddress.equals(""))
            throw new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Not all fields have been specified.");

        //TODO: Consider if this check is necessary. Having it only in the view should be fine.
        if (!password.equals(repeatedPassword))
        {
            throw new HttpServerErrorException(HttpStatus.PRECONDITION_FAILED, "Passwords are not the same!");
        }

        if (!this.userService.isLoginAvailable(username))
        {
            throw new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "This login is already in use!");
        }
        if (!this.userService.isEmailAvailable(emailAddress))
        {
            throw new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "This email is already in use!");
        }

//        final Object nameTest = httpServletRequest.getSession().getAttribute("username");
//        httpServletRequest.getSession().setAttribute("username", "Nerdi");
//        httpServletResponse.addCookie(new Cookie("username", "Nerdi"));
        final User user = this.userService.register(username, password, emailAddress);
        return user;
//        return new User(1, "Test", 1, "Test", "Test");
    }
}
