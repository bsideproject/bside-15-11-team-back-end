package com.beside.startrail.sign.apple.service;

import com.beside.startrail.sign.apple.model.AppleIdToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class AppleSignService {
  private static final ObjectMapper UNKNOWN_IGNORE_OBJECT_MAPPER = new ObjectMapper()
      .configure(
          DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false
      );

  public AppleIdToken getPayload(String jwt) throws IOException {
    String[] splittedJwt = jwt.split("\\.");

    return UNKNOWN_IGNORE_OBJECT_MAPPER.readValue(
        Base64.getDecoder().decode(splittedJwt[1]),
        AppleIdToken.class
    );
  }
}
