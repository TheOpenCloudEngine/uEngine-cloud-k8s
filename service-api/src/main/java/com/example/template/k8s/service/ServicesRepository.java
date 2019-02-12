package com.example.template.k8s.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ServicesRepository extends CrudRepository<Services, String> {

    Iterable<Services> findByProvider(@Param("provider") String provider);
    Iterable<Services> findByProviderAndName(@Param("provider") String provider,@Param("name") String name);
}
