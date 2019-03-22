package com.example.template.config;

import com.example.template.user.User;
import com.example.template.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Autowired
    UserRepository userRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        User user = userRepository.findByUsername(((UserDetails)authentication.getPrincipal()).getUsername() );

        final Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("organization", "Uengine-Oauth");
        additionalInfo.put("email", user.getEmail());
        additionalInfo.put("nickname", user.getNickname());
        additionalInfo.put("thumbnail", user.getThumbnail());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
