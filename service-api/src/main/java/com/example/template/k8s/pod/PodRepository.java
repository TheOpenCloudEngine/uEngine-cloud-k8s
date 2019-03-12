package com.example.template.k8s.pod;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PodRepository extends CrudRepository<Pod, String> {

    Iterable<Pod> findByProvider(@Param("provider") String provider);
    Iterable<Pod> findByProviderAndName(@Param("provider") String provider,@Param("name") String name);
    Iterable<Pod> findByNamespace(@Param("namespace") String namespace);
    Iterable<Pod> findByNamespaceAndName(@Param("namespace") String namespace, @Param("name") String name);

    @Transactional
    @Modifying
    @Query("delete from Pod")
    void deleteAllInQuery();
}
