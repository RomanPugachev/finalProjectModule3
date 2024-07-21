package org.example.finalprojectmodule3.service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojectmodule3.model.Subject;
import org.example.finalprojectmodule3.model.Teacher;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.model.dto.TeacherDTO;

import java.util.Collection;
import java.util.Map;


@RequiredArgsConstructor
public class TeacherService {
    private final MemoryDB memoryDB;
    private final MappingService mappingService;

    public Collection<Teacher> getAllTeachers(){
        return this.memoryDB.getTeachers().values();
    }

    public void addTeacherSubject(Long teacherId, String subjectName) {
        Subject newSubject = Subject.getByName(subjectName);
        memoryDB.getTeachers().get(teacherId).addSubject(newSubject);
    }

    public void addTeacher(TeacherDTO teacherDTO){
        memoryDB.getTeachers().put(teacherDTO.getId(), mappingService.fromTeacherDTO(teacherDTO));
    }
}
