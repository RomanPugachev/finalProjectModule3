package org.example.finalprojectmodule3.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.example.finalprojectmodule3.consts.Consts.*;

@Data
@Builder
@NoArgsConstructor
public class Student {
    @Builder.Default
    private Long id = null;
    @Builder.Default
    private String name = "Ivan";
    @Builder.Default
    private String patronymic = "Ivanovich";
    @Builder.Default
    private String surname = "Ivanov";
    @Builder.Default
    private LocalDate birthDate = LocalDate.of(2000, 1, 1);
    @Builder.Default
    private String phoneNumber = "8 000 000 00 00";

    @JsonCreator
    public Student(@JsonProperty(ID) Long id, @JsonProperty(NAME) String name,
                   @JsonProperty(PATRONYMIC) String patronymic, @JsonProperty(SURNAME) String surname,
                   @JsonProperty(BIRTHDATE) LocalDate birthDate, @JsonProperty(PHONE_NUMBER) String phoneNumber) {
        this.id = id;
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append(ID, id)
                .append(NAME, name)
                .append(PATRONYMIC, patronymic)
                .append(SURNAME, surname)
                .append(BIRTHDATE, birthDate)
                .append(PHONE_NUMBER, phoneNumber)
                .toString();
    }
}


