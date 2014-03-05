package org.springular.web.security;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Naor Yuval
 * Date: 12/10/13
 * Sets cookies on successful login:
 * 1. Sets a cookie with user name and role for role-based navigation on angularJS
 * 2. Sets an XSRF-TOKEN cookie (See: http://docs.angularjs.org/api/ng.$http)
 */
public class SetCookieAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    static final Logger logger = LoggerFactory.getLogger(SetCookieAuthenticationSuccessHandler.class);

    private String defaultTargetUrl;
    public void setDefaultTargetUrl(String foo){ defaultTargetUrl = foo; }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        // create JSON object with the user userId and roles:
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode obj = factory.objectNode();
        ArrayNode arrayNode = new ArrayNode(factory);

        Iterator iterator = ((UsernamePasswordAuthenticationToken)authentication).getAuthorities().iterator();
        while (iterator.hasNext()) {
            arrayNode.add(iterator.next().toString());
        }

        obj.put("user", authentication.getPrincipal().toString());
        obj.put("roles", arrayNode);

        Cookie xsrfCookie = new Cookie("XSRF-TOKEN", UUID.randomUUID().toString());
        Cookie userCookie = new Cookie("USER", obj.toString());
        logger.info("Setting USER cookie with: "+obj.toString());
        httpServletResponse.addCookie(userCookie);
        httpServletResponse.addCookie(xsrfCookie);

        httpServletResponse.sendRedirect(defaultTargetUrl);
    }
}
