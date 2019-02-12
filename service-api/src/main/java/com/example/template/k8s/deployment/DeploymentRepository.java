package com.example.template.k8s.deployment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DeploymentRepository extends CrudRepository<Deployment, String> {

    Iterable<Deployment> findByProvider(@Param("provider") String provider);
    Iterable<Deployment> findByProviderAndName(@Param("provider") String provider,@Param("name") String name);
}
