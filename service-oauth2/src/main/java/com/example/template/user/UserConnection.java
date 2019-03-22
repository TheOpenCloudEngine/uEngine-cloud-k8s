package com.example.template.user;

import com.example.template.social.google.GoogleUserDetails;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
public class UserConnection {

    private long id;
    private String email;
    private ProviderType provider;
    private String providerId;
    private String displayName;
    private String profileUrl;
    private String imageUrl;
    private String accessToken;
    private long expireTime;

    @Builder
    private UserConnection(String email, ProviderType provider, String providerId, String displayName, String profileUrl, String imageUrl, String accessToken, long expireTime) {
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
        this.imageUrl = imageUrl;
        this.accessToken = accessToken;
        this.expireTime = expireTime;
    }

    public static UserConnection valueOf(GoogleUserDetails userDetails) {
        return UserConnection.builder()
                .expireTime(userDetails.getExpiration())
                .accessToken(userDetails.getAccess_token())
                .providerId(userDetails.getSub())
                .email(userDetails.getEmail())
                .displayName(userDetails.getName())
                .imageUrl(userDetails.getPicture())
                .provider(ProviderType.GOOGLE)
                .profileUrl(userDetails.getProfile())
                .build();
    }


}
