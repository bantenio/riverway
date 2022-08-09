package com.yomahub.liteflow.example.buffer.jackson;

import com.yomahub.liteflow.example.buffer.Buffer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;

public class BufferDeserializer extends JsonDeserializer<Buffer> {
    public static final Base64.Decoder BASE64_DECODER;

    static {
        BASE64_DECODER = Base64.getUrlDecoder();
    }

    @Override
    public Buffer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String text = p.getText();
        try {
            return Buffer.buffer(BASE64_DECODER.decode(text));
        } catch (IllegalArgumentException e) {
            throw new InvalidFormatException(p, "Expected a base64 encoded byte array", text, Instant.class);
        }
    }
}
