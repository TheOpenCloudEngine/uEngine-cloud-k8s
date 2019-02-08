package com.example.template.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface InstanceModelRepository extends CrudRepository<InstanceModel, String> {

    Iterable<InstanceModel> findByProvider(@Param("provider") String provider);
    Iterable<InstanceModel> findByProviderAndName(@Param("provider") String provider,@Param("name") String name);
}
