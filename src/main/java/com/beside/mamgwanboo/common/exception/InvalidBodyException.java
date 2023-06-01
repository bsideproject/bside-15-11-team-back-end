package com.beside.mamgwanboo.common.exception;

import com.google.protobuf.InvalidProtocolBufferException;

public class InvalidBodyException extends RuntimeException {
  public InvalidBodyException(String message,
                              InvalidProtocolBufferException invalidProtocolBufferException) {
    super(message, invalidProtocolBufferException);
  }
}
