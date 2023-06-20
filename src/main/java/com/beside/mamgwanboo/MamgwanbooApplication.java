package com.beside.mamgwanboo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@EnableReactiveMongoAuditing
@EnableMongoAuditing
@SpringBootApplication
public class MamgwanbooApplication {
  public static void main(String[] args) {
    SpringApplication.run(MamgwanbooApplication.class, args);
  }
}
