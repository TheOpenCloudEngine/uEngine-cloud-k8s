package com.example.template.k8s.user;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "user_detail")
public class UserDetail {
    @Id
    String username;

    String host;

    @Column(columnDefinition = "TEXT")
    String token;

}
