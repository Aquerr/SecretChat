package io.github.aquerr.secretchat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aquerr.secretchat.models.User;
import io.github.aquerr.secretchat.services.UserService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
//@WebMvcTest(LoginController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    public void getRegisterTest() throws Exception
    {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/register"))
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Zarejestruj Się")));
    }

    @Test
    public void postRegisterTest() throws Exception
    {
        deleteTestUserIfExists();

        final Map<String, Object> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "testpassword");
        credentials.put("repeatedPassword", "testpassword");
        credentials.put("email", "test@mail.com");
        ObjectMapper mapper = new ObjectMapper();
        String jsonCredentialsString = mapper.writeValueAsString(credentials);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/register").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(jsonCredentialsString))
//                .andDo(MockMvcResultHandlers.print(System.out))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("testuser")));

        deleteTestUserIfExists();
    }

    @Test
    public void getLoginTest() throws Exception
    {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/login"))
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Zaloguj Się")));
    }

    @Test
    public void loginTest() throws Exception
    {
        addTestUserIfNotExists();

        final Map<String, Object> credentials = new HashMap<>();
        credentials.put("login", "testuser");
        credentials.put("password", "testpassword");
        ObjectMapper mapper = new ObjectMapper();
        String jsonCredentialsString = mapper.writeValueAsString(credentials);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/login").accept(MediaType.TEXT_PLAIN_VALUE).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(jsonCredentialsString))
//                .andDo(MockMvcResultHandlers.print(System.out))
                .andExpect(MockMvcResultMatchers.request().sessionAttribute("username", Matchers.notNullValue())).andReturn();

        deleteTestUserIfExists();
    }

    private void deleteTestUserIfExists()
    {
        final User user = this.userService.getUser("testuser");
        if(user != null)
        {
            userService.deleteUser(user.getId());
        }
    }

    private void addTestUserIfNotExists()
    {
        if(this.userService.getUser("testuser") == null)
        {
            this.userService.register("testuser", "testpassword", "test@mail.com");
        }
    }
}
