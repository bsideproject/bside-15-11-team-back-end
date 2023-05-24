package com.beside.mamgwanboo.hello;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document("hello")
public class Hello {
  @Id
  private String id;

  private String body;
}
