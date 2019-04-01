package com.example.template.user;

import com.example.template.config.AuthorizationServerConfig;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//@RestController
public class UserController {

//    @Autowired
    TokenEndpoint tokenEndpoint;

//    @RequestMapping(value = "/", method = RequestMethod.GET)
    public OAuth2AccessToken home(HttpServletRequest request, Principal principal) {

        String CLIENT_ID = AuthorizationServerConfig.CLIENT_ID;
        String CLIENT_SECRET = AuthorizationServerConfig.CLIENT_SECRET;

        if( principal == null ){
            return null;
        }

        String username = ((User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getUsername();
        String password = "password";
//        String password = ((User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getPassword();


        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "password");
        parameters.put("username", username);
        parameters.put("password", password);

        // Create principal and auth token
        UsernamePasswordAuthenticationToken principals = new UsernamePasswordAuthenticationToken(CLIENT_ID, CLIENT_SECRET, AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        try {
            ResponseEntity<OAuth2AccessToken> accessToken = tokenEndpoint.postAccessToken(principals, parameters);
//            System.out.println(accessToken.getBody());
            return accessToken.getBody();
        } catch (HttpRequestMethodNotSupportedException e) {
            e.printStackTrace();
        }

        return null;

    }


}
