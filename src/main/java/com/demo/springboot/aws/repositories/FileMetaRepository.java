package com.demo.springboot.aws.repositories;

import com.demo.springboot.aws.models.FileMeta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaRepository extends CrudRepository<FileMeta, Integer> {
}
