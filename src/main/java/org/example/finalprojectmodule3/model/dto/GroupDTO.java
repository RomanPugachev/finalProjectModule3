package org.example.finalprojectmodule3.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.finalprojectmodule3.model.Student;

import java.util.List;

import static org.example.finalprojectmodule3.consts.Consts.*;

@Data
public class GroupDTO {
    @JsonProperty("number")
    private String groupNumber;
    @JsonProperty("students")
    private List<Long> studentsId;
}
