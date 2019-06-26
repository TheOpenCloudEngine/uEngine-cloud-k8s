package com.example.template.List;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ListRepository extends CrudRepository<List, Long> {
   
   List<List> findByUsername(@Param("username") String username);
   
   List<List> findAllByUsername(@Param("username") Iterable<String> username);
   
}
