package com.beside.mamgwanboo.common.service;

import protobuf.sign.MamgwanbooJwtPayload;

public interface JwtService {
  String makeMamgwanbooJwt(
      MamgwanbooJwtPayload mamgwanbooJwtPayload
  );
}
