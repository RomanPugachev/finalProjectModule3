package org.example.finalprojectmodule3.model.dto;

import lombok.Data;
import org.example.finalprojectmodule3.model.Subject;

import java.util.ArrayList;

@Data
public class TeacherDTO {
    Long id;
    String name;
    Long experience;
    ArrayList<Subject> subjects;
}
