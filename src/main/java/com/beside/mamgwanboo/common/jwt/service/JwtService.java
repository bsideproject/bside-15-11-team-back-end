package com.beside.mamgwanboo.common.jwt.service;

import protobuf.sign.MamgwanbooJwtPayload;

public interface JwtService {
	String makeMamgwanbooJwt(
		MamgwanbooJwtPayload mamgwanbooJwtPayload
	);
}
