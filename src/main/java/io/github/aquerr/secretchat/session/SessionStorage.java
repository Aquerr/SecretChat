package io.github.aquerr.secretchat.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SessionStorage
{
    private static final Map<String, HttpSession> sessions = new HashMap<>();

    public static List<HttpSession> getActiveSessions()
    {
        return new ArrayList<>(sessions.values());
    }

    @Bean
    public HttpSessionListener httpSessionListener()
    {
        return new HttpSessionListener()
        {
            @Override
            public void sessionCreated(HttpSessionEvent se)
            {
                sessions.put(se.getSession().getId(), se.getSession());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se)
            {
                sessions.remove(se.getSession().getId());
            }
        };
    }
}
