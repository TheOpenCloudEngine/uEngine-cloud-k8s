package com.example.template.config;

import com.example.template.Application;
import com.example.template.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
//@NoArgsConstructor
public class CustomAuthenticationSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            if (authentication.isAuthenticated()) {

                String CLIENT_ID = AuthorizationServerConfig.CLIENT_ID;
                String CLIENT_SECRET = AuthorizationServerConfig.CLIENT_SECRET;

                String username = ((User) ((UsernamePasswordAuthenticationToken) authentication).getPrincipal()).getUsername();
                String password = "password";
//              String password = ((User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getPassword();


                Map<String, String> parameters = new HashMap<>();
                parameters.put("grant_type", "password");
                parameters.put("username", username);
                parameters.put("password", password);

                // Create principal and auth token
                UsernamePasswordAuthenticationToken principals = new UsernamePasswordAuthenticationToken(CLIENT_ID, CLIENT_SECRET, AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
                try {
                    TokenEndpoint tokenEndpoint = Application.applicationContext.getBean(TokenEndpoint.class);
                    ResponseEntity<OAuth2AccessToken> accessToken = tokenEndpoint.postAccessToken(principals, parameters);
//                    System.out.println(accessToken.getBody());
//                    return accessToken.getBody();

                    response.sendRedirect(request.getHeader("referer") + "?access_token=" + accessToken.getBody());
                } catch (HttpRequestMethodNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
