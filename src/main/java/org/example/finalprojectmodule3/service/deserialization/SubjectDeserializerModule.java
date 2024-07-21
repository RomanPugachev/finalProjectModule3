package org.example.finalprojectmodule3.service.deserialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.example.finalprojectmodule3.model.Subject;

import java.io.IOException;
import java.util.Arrays;

public class SubjectDeserializerModule extends SimpleModule {
    public SubjectDeserializerModule(){
        super("SubjectDeserializerModule");
        addDeserializer(Subject.class, new SubjectDeserializer());
        addSerializer(Subject.class, new SubjectSerializer());

    }

    @Override
    public String getModuleName() {
        return "SubjectDeserializerModule";
    }

    @Override
    public Version version() {
        return new Version(1, 0, 0, "");
    }

    // Класс для десериализации объектов subject
    private static class SubjectDeserializer extends StdDeserializer<Subject> {
        public SubjectDeserializer(){
            super(Subject.class);
        }

        @Override
        public Subject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            String nameOfSubject = node.asText();
            return Arrays.stream(Subject.values())
                    .filter(subject -> subject.getNameOfSubject().equals(nameOfSubject))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid name of subject: " + nameOfSubject));
        }
    }
    // Класс для сериализации объектов subject
    private static class SubjectSerializer extends StdSerializer<Subject> {
        public SubjectSerializer() {
            super(Subject.class);
        }

        @Override
        public void serialize(Subject value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.getNameOfSubject());
        }
    }
}
