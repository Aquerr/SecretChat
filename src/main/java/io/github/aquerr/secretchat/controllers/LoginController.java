package io.github.aquerr.secretchat.controllers;

import io.github.aquerr.secretchat.models.User;
import io.github.aquerr.secretchat.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class LoginController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("([A-Za-z_.]*)@([A-Za-z]*)\\.([A-Za-z]*)");

    private final UserService userService;

    public LoginController(final UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, final Model model)
    {
        if(httpServletRequest.getSession().getAttribute("username") != null)
        {
            try
            {
                httpServletResponse.sendRedirect("/myprofile");
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        model.addAttribute("content", "login");
        model.addAttribute("title", "Zaloguj");
        return "main";
    }

    @GetMapping("/register")
    public String register(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, final Model model)
    {
        if(httpServletRequest.getSession().getAttribute("username") != null)
        {
            try
            {
                httpServletResponse.sendRedirect("/myprofile");
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        model.addAttribute("content", "register");
        model.addAttribute("title", "Zarejestruj");
        return "main";
    }

    @ResponseBody
    @PostMapping(path = "/login", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> login(@RequestBody(required = false) final Map<String, Object> credentials, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, final Model model) throws IOException
    {
        if(credentials == null || !credentials.containsKey("login") || !credentials.containsKey("password"))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payload does not contain login and password!");
        }

        final String login = String.valueOf(credentials.get("login"));
        final String password = String.valueOf(credentials.get("password"));

        boolean isMail = false;

        //If login = mail
        if (EMAIL_PATTERN.matcher(login).matches())
        {
            //Process mail
            isMail = true;
        }

        //TODO: Successfully logged in user should get a authentication token instead of username cookie. (I suppose)

        User user = null;

        if (isMail)
        {
            user = this.userService.login("", login, password);
        }
        else
        {
            user = this.userService.login(login, "", password);
        }

        if (user != null)
        {
            httpServletRequest.getSession().setAttribute("username", user.getName());
//            model.addAttribute("username", user.getGender());
        }
        else
        {
//            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//            httpServletResponse.getWriter().println("BOOM");
//            httpServletResponse.getWriter().flush();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nie znaleziono konta o podanej nazwie użytkownika lub adresie e-mail.");
//            return;
        }
        LOGGER.info(user.getName() + " has logged in.");
//        model.addAttribute("content", "index");
//        httpServletResponse.setStatus(HttpStatus.OK.value());
//        httpServletResponse.getWriter().println(user.getId());
        return ResponseEntity.ok(String.valueOf(user.getId()));
    }

    @ResponseBody
    @PostMapping(path = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@RequestBody final Map<String, Object> jsonNode, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException
    {
        LOGGER.info("Registering...");
        final String username = String.valueOf(jsonNode.get("username"));
        final String password = String.valueOf(jsonNode.get("password"));
        final String repeatedPassword = String.valueOf(jsonNode.get("repeatedPassword"));
        final String emailAddress = String.valueOf(jsonNode.get("email"));

        if (!EMAIL_PATTERN.matcher(emailAddress).matches())
            return ResponseEntity.badRequest().body("Email Address is not in correct format.");
        if (username.equals("") || password.equals("") || repeatedPassword.equals("") || emailAddress.equals(""))
            return ResponseEntity.badRequest().body("Not all fields have been specified.");
        if (!password.equals(repeatedPassword))
            return ResponseEntity.badRequest().body("Passwords are not the same!");
        if (!this.userService.isLoginAvailable(username))
            return ResponseEntity.badRequest().body("This login is already in use!");
        if (!this.userService.isEmailAvailable(emailAddress))
            return ResponseEntity.badRequest().body("This email is already in use!");

        final User user = this.userService.register(username, password, emailAddress);
        LOGGER.info(username + " successfully registered.");
        return ResponseEntity.ok(user);
    }

    @GetMapping(path = "logout")
    public String logout(final Model model, final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse)
    {
        httpServletRequest.getSession().removeAttribute("username");
        httpServletRequest.getSession().invalidate();
        model.addAttribute("content", "index");
        return "main";
    }
}
