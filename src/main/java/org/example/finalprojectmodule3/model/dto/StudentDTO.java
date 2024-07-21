package org.example.finalprojectmodule3.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {
    public Long id;
    public String name;
    public String surname;
    public String patronymic;
    public String phoneNumber;
}
