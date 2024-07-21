package org.example.finalprojectmodule3.service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojectmodule3.model.Student;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.model.dto.StudentDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StudentService {
    private final MemoryDB db;
    private final MappingService mappingService;

    public Collection<Student> getAllStudents(){
        return this.db.getStudents().values();
    }

    public void addStudent(StudentDTO studentDTO){
        db.getStudents().put(studentDTO.getId(), mappingService.fromStudentDTO(studentDTO));
    }

    public Student getStudentById(Long id){
        if(db.getStudents().containsKey(id)) {
            return this.db.getStudents().get(id);
        }
        return null;
    }

    public Collection<Student> getStudentsBySurname(String surname){
        return db.getStudents().values().stream()
                .filter(x -> x.getSurname().equals(surname))
                .collect(Collectors.toList());
    }

    public void deleteStudentById(Long id){
        db.getStudents().remove(id);
    }

    public void updateStudentById(StudentDTO studentDTO, Long id){
        if(db.getStudents().containsKey(id)) {
            var birthday = db.getStudents().get(id).getBirthDate();
            studentDTO.setId(id);
            db.getStudents().put(id, mappingService.fromStudentDTO(studentDTO));
            db.getStudents().get(id).setBirthDate(birthday);
        } else {
            throw new RuntimeException("Студент с таким id не обнаружен");
        }
    }
}
