package com.yomahub.liteflow.example.context.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.yomahub.liteflow.example.context.DataObject;

import java.io.IOException;

public class DataObjectSerializer extends JsonSerializer<DataObject> {
    @Override
    public void serialize(DataObject value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeObject(value.getMap());
    }
}
