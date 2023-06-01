package com.beside.mamgwanboo.common.util;

import com.beside.mamgwanboo.common.exception.InvalidBodyException;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import reactor.core.publisher.Mono;

public final class ProtocolBufferUtil {
	private ProtocolBufferUtil() {
	}

	public static <T extends Message> Mono<T> parse(String body, T.Builder tBuilder) {
		try {
			JsonFormat.parser().merge(body, tBuilder);

			return Mono.just((T)tBuilder.build());
		} catch (InvalidProtocolBufferException invalidProtocolBufferException) {
			return Mono.error(new InvalidBodyException(String.format("body 파싱에 실패했습니다. body: %s, type: %s", body, tBuilder.getDescriptorForType()), invalidProtocolBufferException));
		}
	}
}
