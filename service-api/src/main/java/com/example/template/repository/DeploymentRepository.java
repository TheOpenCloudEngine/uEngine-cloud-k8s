package com.example.template.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.template.model.Deployment;

public interface DeploymentRepository extends CrudRepository<Deployment, String> {

    Iterable<Deployment> findByProvider(@Param("provider") String provider);
    Iterable<Deployment> findByProviderAndName(@Param("provider") String provider,@Param("name") String name);
}
