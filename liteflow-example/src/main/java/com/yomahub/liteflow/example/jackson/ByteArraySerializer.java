package com.yomahub.liteflow.example.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Base64;

public class ByteArraySerializer extends JsonSerializer<byte[]> {
    public static final Base64.Encoder BASE64_ENCODER;

    static {
        BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();
    }

    @Override
    public void serialize(byte[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(BASE64_ENCODER.encodeToString(value));
    }
}
