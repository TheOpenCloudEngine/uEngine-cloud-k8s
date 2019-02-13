package com.example.template.k8s.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.template.k8s.deployment.Deployment;

public interface ServicesRepository extends CrudRepository<Services, String> {

    Iterable<Services> findByProvider(@Param("provider") String provider);
    Iterable<Services> findByProviderAndName(@Param("provider") String provider,@Param("name") String name);
    Iterable<Services> findByNamespace(@Param("namespace") String namespace);
    Iterable<Services> findByNamespaceAndName(@Param("namespace") String namespace, @Param("name") String name);
}
