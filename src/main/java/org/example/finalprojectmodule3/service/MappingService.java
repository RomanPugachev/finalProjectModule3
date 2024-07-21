package org.example.finalprojectmodule3.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.finalprojectmodule3.model.Group;
import org.example.finalprojectmodule3.model.Student;
import org.example.finalprojectmodule3.model.Subject;
import org.example.finalprojectmodule3.model.Teacher;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.model.dto.GroupDTO;
import org.example.finalprojectmodule3.model.dto.StudentDTO;
import org.example.finalprojectmodule3.model.dto.TeacherDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class MappingService {
    private MemoryDB memoryDB;
     public Student fromStudentDTO(StudentDTO studentDTO) {
        return Student.builder()
                .id(studentDTO.getId())
                .name(studentDTO.getName())
                .patronymic(studentDTO.getPatronymic())
                .surname(studentDTO.getSurname())
                .birthDate(LocalDate.now())
                .phoneNumber(studentDTO.getPhoneNumber())
                .build();
    }

    public Teacher fromTeacherDTO(TeacherDTO teacherDTO){
         return Teacher.builder()
                 .id(teacherDTO.getId())
                 .name(teacherDTO.getName())
                 .experience(teacherDTO.getExperience())
                 .subjects(teacherDTO.getSubjects())
                 .build();
    }

    public Group fromGroupDTO(GroupDTO groupDTO, Long appropriateId, List<Student> studentsForGroup){
         return Group.builder()
                .id(appropriateId)
                .groupNumber(groupDTO.getGroupNumber())
                .students(studentsForGroup)
                .build();
    }
}
