package com.beside.startrail.common.command;

public interface Command <R, P> {
  R execute(P p);
}
