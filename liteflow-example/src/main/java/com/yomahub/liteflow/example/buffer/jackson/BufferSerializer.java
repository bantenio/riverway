package com.yomahub.liteflow.example.buffer.jackson;

import com.yomahub.liteflow.example.buffer.Buffer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Base64;

public class BufferSerializer extends JsonSerializer<Buffer> {

    public static final Base64.Encoder BASE64_ENCODER;

    static {
        BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();
    }

    @Override
    public void serialize(Buffer value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(BASE64_ENCODER.encodeToString(value.getBytes()));
    }
}
