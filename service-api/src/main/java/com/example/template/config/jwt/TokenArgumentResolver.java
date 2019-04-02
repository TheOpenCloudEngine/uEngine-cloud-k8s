package com.example.template.config.jwt;

import com.example.template.k8s.user.UserDetail;
import com.example.template.k8s.user.UserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.core.MethodParameter;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(TokenArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return UserDetail.class.isAssignableFrom(parameter.getParameterType());
    }

    public TokenArgumentResolver() {
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer container,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        UserDetail token = new UserDetail();


        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Optional<String> accessToken = Optional.ofNullable(request.getHeader("authorization"));

        logger.info("Token {}", accessToken.get());

        return accessToken.isPresent()? accessToken.map(t->{

            t = t.replace("Bearer ","").replace("bearer ","");

            Jwt jwt = JwtHelper.decode(t);

            JsonParser parser = new JacksonJsonParser();
            Map<String, Object> map = parser.parseMap(jwt.getClaims());

            token.setUsername(map.getOrDefault("user_name","").toString());

            return token;
        }).orElse(null) : null;

    }
}
