package com.beside.mamgwanboo.hello;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelloRepository extends MongoRepository<Hello, Long> {
  Optional<Hello> findByBody(String body);
}
