package com.beside.startrail.common.command;

public interface Command <R, P> {
  // todo 이걸로 어떻게 추상화 해서 execute로 통일되게
  R execute(P p);
}
