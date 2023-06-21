package com.beside.startrail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@EnableReactiveMongoAuditing
@SpringBootApplication
public class StartrailApplication {
  public static void main(String[] args) {
    SpringApplication.run(StartrailApplication.class, args);
  }
}
