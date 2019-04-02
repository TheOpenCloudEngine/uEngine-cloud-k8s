package com.example.template.k8s.service;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ServicesRepository extends CrudRepository<Services, String> {

    Iterable<Services> findByHost(@Param("host") String host);
    Iterable<Services> findByHostAndNamespace(@Param("host") String host, @Param("namespace") String namespace);
    Iterable<Services> findByHostAndNamespaceAndName(@Param("host") String host, @Param("namespace") String namespace, @Param("name") String name);

    @Transactional
    @Modifying
    @Query("delete from Services")
    void deleteAllInQuery();

    @Modifying
    @Transactional
    @Query(value="DELETE FROM SERVICES WHERE host=?1",
            nativeQuery = true)
    void deleteByHost(@Param("host") String host);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM SERVICES WHERE host=?1 and namespace=?2 and name =?3 ",
            nativeQuery = true)
    void deleteService(@Param("host") String host, @Param("namespace") String namespace, @Param("name") String name);
}
