package com.yomahub.liteflow.example.context.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.yomahub.liteflow.example.context.DataArray;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

public class DataArraySerializer extends JsonSerializer<DataArray> {
    @Override
    public void serialize(DataArray value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeObject(value.getList());
    }
}

