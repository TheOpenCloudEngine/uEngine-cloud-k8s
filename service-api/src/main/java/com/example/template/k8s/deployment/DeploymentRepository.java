package com.example.template.k8s.deployment;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DeploymentRepository extends CrudRepository<Deployment, String> {

    Iterable<Deployment> findByProvider(@Param("provider") String provider);
    Iterable<Deployment> findByProviderAndName(@Param("provider") String provider,@Param("name") String name);
    Iterable<Deployment> findByNamespace(@Param("namespace") String namespace);
    Iterable<Deployment> findByNamespaceAndName(@Param("namespace") String namespace, @Param("name") String name);

    @Transactional
    @Modifying
    @Query("delete from Deployment")
    void deleteAllInQuery();
}
