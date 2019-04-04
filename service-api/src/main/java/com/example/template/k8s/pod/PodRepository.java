package com.example.template.k8s.pod;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PodRepository extends CrudRepository<Pod, String> {

    Iterable<Pod> findByProvider(@Param("provider") String provider);
    Iterable<Pod> findByProviderAndName(@Param("provider") String provider,@Param("name") String name);
    Iterable<Pod> findByHost(@Param("host") String host);
    Iterable<Pod> findByHostAndNamespace(@Param("host") String host, @Param("namespace") String namespace);
    Iterable<Pod> findByHostAndNamespaceAndName(@Param("host") String host, @Param("namespace") String namespace, @Param("name") String name);
    Iterable<Pod> findByNamespaceAndNameLike(@Param("namespace") String namespace, @Param("name") String name);

    @Transactional
    @Modifying
    @Query("delete from Pod")
    void deleteAllInQuery();

    @Modifying
    @Transactional
    @Query(value="DELETE FROM POD WHERE host=?1",
            nativeQuery = true)
    void deleteByHost(@Param("host") String host);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM POD WHERE host=?1 and namespace=?2 and name =?3 ",
            nativeQuery = true)
    void deletePod(@Param("host") String host, @Param("namespace") String namespace, @Param("name") String name);
}
