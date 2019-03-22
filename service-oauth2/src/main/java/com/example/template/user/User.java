package com.example.template.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String username;
    @Column
    @JsonIgnore
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "provider_id")
    private String social;

    @Builder
    private User(String username, String password, String email, String nickname, String social, String thumbnail) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.social = social;
        this.thumbnail = thumbnail;
    }

    public static User signUp(UserConnection userConnection) {

        String defaultPassword = "password";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(defaultPassword);

        return User.builder()
                .username(userConnection.getEmail())
                .email(userConnection.getEmail())
                .nickname(userConnection.getDisplayName())
                .social(userConnection.getProviderId())
                .thumbnail(userConnection.getImageUrl())
//                .password(defaultPassword)   // default
                .password(encodedPassword)   // default
                .build();

    }


}