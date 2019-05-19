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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class LoginController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private final UserService userService;

    private Pattern emailPattern = Pattern.compile("([A-Za-z_.]*)@([A-Za-z]*)\\.([A-Za-z]*)");

    public LoginController(final UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(final Model model)
    {
        model.addAttribute("content", "login");
        model.addAttribute("title", "Zaloguj");
        return "main";
    }

    @GetMapping("/register")
    public String register(final Model model)
    {
        model.addAttribute("content", "register");
        model.addAttribute("title", "Zarejestruj");
        return "main";
    }

    @ResponseBody
    @PostMapping(path = "/login")
    public ModelAndView login(final String login, final String password, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, final Model model)
    {
        boolean isMail = false;

        //If login = mail
        if (emailPattern.matcher(login).matches())
        {
            //Process mail
            isMail = true;
        }

        //TODO: Successfully logged in user should get a authentication token instead of username cookie. (I suppose)

//        httpServletRequest.getSession().setAttribute("username", "name");
//        httpServletRequest.authenticate(httpServletResponse);

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
//            final HttpSession session = httpServletRequest.getSession(true);
//            httpServletResponse.addCookie(new Cookie("session", session.getId()));
            httpServletRequest.getSession().setAttribute("username", user.getName());
            model.addAttribute("username", user.getName());
//            UserService.INLOGGED_USERS.put(session.getId(), user.getName());
        }
        else
        {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not find a user with such login/mail.");
            try
            {
                httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not find a user with such login/mail.");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return new ModelAndView("login");
        }
        LOGGER.info(user.getName() + " has logged in.");
        model.addAttribute("content", "index");

//        return ResponseEntity.ok("Logged In");
//        return null;
        return new ModelAndView("main");
    }

//    @ResponseBody
//    @PostMapping(path = "/login", consumes = "application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public User login(@RequestBody final Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
//    {
//        if (!map.containsKey("password") || String.valueOf(map.get("password")).equals(""))
//            throw new HttpServerErrorException(HttpStatus.PRECONDITION_FAILED, "Password must be specified!");
//
//
//        if (!map.containsKey("login"))
//            throw new HttpServerErrorException(HttpStatus.PRECONDITION_FAILED, "Username or email must be specified!");
//
//        final String login = String.valueOf(map.get("login"));
//        final String password = String.valueOf(map.get("password"));
//        boolean isMail = false;
//
//        //If login = mail
//        if (emailPattern.matcher(login).matches())
//        {
//            //Process mail
//            isMail = true;
//        }
//
//        //TODO: Successfully logged in user should get a authentication token instead of username cookie. (I suppose)
//
////        httpServletRequest.getSession().setAttribute("username", "name");
////        httpServletRequest.authenticate(httpServletResponse);
//
//        if (isMail)
//        {
//            final User user = this.userService.login("", login, password);
//            if (user != null)
//            {
//                HttpSession session = httpServletRequest.getSession(true);
//                session.setAttribute("username", user.getName());
////                session.setAttribute("SecretChat-Token", "Token...");
//                return user;
//            }
//            else
//            {
//                return null;
//            }
//        }
//        else
//        {
//            return this.userService.login(login, "", password);
//        }
//    }

    @ResponseBody
    @PostMapping(path = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@RequestBody final Map<String, Object> jsonNode, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException
    {
        LOGGER.info("Registering...");
//        final Principal userPrincipal = httpServletRequest.getUserPrincipal();
//        final Cookie[] cookies = httpServletRequest.getCookies();
//        try
//        {
//            httpServletRequest.authenticate(httpServletResponse);
//            httpServletRequest.login("test", "test");
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        catch (ServletException e)
//        {
//            e.printStackTrace();
//        }
//        final Object test = WebUtils.getCookie(httpServletRequest, "username");

        final String username = String.valueOf(jsonNode.get("username"));
        final String password = String.valueOf(jsonNode.get("password"));
        final String repeatedPassword = String.valueOf(jsonNode.get("repeatedPassword"));
        final String emailAddress = String.valueOf(jsonNode.get("email"));

        if (!emailPattern.matcher(emailAddress).matches())
        {
            return ResponseEntity.badRequest().body("Email Address is not in correct format.");
        }

        if (username.equals("") || password.equals("") || repeatedPassword.equals("") || emailAddress.equals(""))
        {
//            httpServletResponse.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Not all fields have been specified.");
            return ResponseEntity.badRequest().body("Not all fields have been specified.");
        }

        //TODO: Consider if this check is necessary. Having it only in the view should be fine.
        if (!password.equals(repeatedPassword))
        {
//            httpServletResponse.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Passwords are not the same!");
            return ResponseEntity.badRequest().body("Passwords are not the same!");
        }

        if (!this.userService.isLoginAvailable(username))
        {
//            httpServletResponse.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "This login is already in use!");
            return ResponseEntity.badRequest().body("This login is already in use!");
        }
        if (!this.userService.isEmailAvailable(emailAddress))
        {
//            httpServletResponse.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "This email is already in use!");
            return ResponseEntity.badRequest().body("This email is already in use!");
        }

//        final Object nameTest = httpServletRequest.getSession().getAttribute("username");
//        httpServletRequest.getSession().setAttribute("username", "Nerdi");
//        httpServletResponse.addCookie(new Cookie("username", "Nerdi"));
        final User user = this.userService.register(username, password, emailAddress);
        LOGGER.info(username + " successfully registered.");
        return ResponseEntity.ok(user);
//        return new User(1, "Test", 1, "Test", "Test");
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
