package io.github.aquerr.secretchat.interceptors;

import io.github.aquerr.secretchat.security.RequireAuthorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerInterceptor extends HandlerInterceptorAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if(handler instanceof HandlerMethod)
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if(handlerMethod.hasMethodAnnotation(RequireAuthorization.class) && request.getSession().getAttribute("username") == null) {
                response.sendRedirect("/login");
                return false;
            }
//            LOGGER.info(((HandlerMethod) handler).getShortLogMessage());
        }

        return true;
    }
}
