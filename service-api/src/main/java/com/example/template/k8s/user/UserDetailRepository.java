package com.example.template.k8s.user;

import org.springframework.data.repository.CrudRepository;

public interface UserDetailRepository extends CrudRepository<UserDetail, String> {
}
