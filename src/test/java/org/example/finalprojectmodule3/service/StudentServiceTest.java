package org.example.finalprojectmodule3.service;

import org.example.finalprojectmodule3.model.Student;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.model.dto.StudentDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    public static StudentService studentServiceForTests;
    public static MappingService mockedMappingService;
    public static MemoryDB mockedDB;
    public static Map<Long, Student> memorisedStudents;
    public static Map<Long, Student> testStudents;

    @BeforeAll
    public static void initObjectsForTests(){
        mockedDB = Mockito.mock(MemoryDB.class);
        memorisedStudents = new HashMap<>();
        mockedMappingService = Mockito.mock(MappingService.class);
        Mockito.when(mockedDB.getStudents()).thenReturn(memorisedStudents);
        memorisedStudents.put(0L, Student.builder().id(0L).build());
        memorisedStudents.put(1L, Student.builder().id(1L).name("Petr").patronymic("Petrovich").surname("Petrov").build());
        testStudents = Map.copyOf(memorisedStudents);
        studentServiceForTests = new StudentService(mockedDB, mockedMappingService);
    }

    @Test
    void testGetAllStudents() {
        var gottenStudents = studentServiceForTests.getAllStudents();
        assertTrue(gottenStudents.stream().allMatch(gottenStudent -> memorisedStudents.containsValue(gottenStudent)));
    }

    @Test
    void addStudent() {
        StudentDTO studentDtoToBeAdded = Mockito.mock(StudentDTO.class);
        Student studentToBeAdded = Mockito.mock(Student.class);
        Mockito.when(mockedMappingService.fromStudentDTO(studentDtoToBeAdded)).thenReturn(studentToBeAdded);
        studentServiceForTests.addStudent(studentDtoToBeAdded);
        assertTrue(memorisedStudents.containsValue(studentToBeAdded));
    }

    @Test
    void getStudentById() {
        assertEquals(studentServiceForTests.getStudentById(0L), memorisedStudents.get(0L));
    }

    @Test
    void getStudentsBySurname() {
        Student studentToSearch = Mockito.mock(Student.class);
        String searchingSurname = "SomeSurname";
        Mockito.when(studentToSearch.getSurname()).thenReturn(searchingSurname);
        Mockito.when(studentToSearch.toString()).thenReturn(searchingSurname);
        memorisedStudents.put(3L, studentToSearch);
        System.out.println(memorisedStudents);
        Student foundStudent = studentServiceForTests.getStudentsBySurname(searchingSurname).stream().findFirst().get();
        assertEquals(studentToSearch, foundStudent);
    }

    @Test
    void deleteStudentById() {
        studentServiceForTests.deleteStudentById(0L);
        assertFalse(memorisedStudents.containsKey(0L));
    }

    @Test
    void updateStudentById() {
        StudentDTO refreshingStudentDTO = Mockito.mock(StudentDTO.class);
        Student refreshingStudent = Mockito.mock(Student.class);
        Mockito.when(mockedMappingService.fromStudentDTO(refreshingStudentDTO)).thenReturn(refreshingStudent);
        studentServiceForTests.updateStudentById(refreshingStudentDTO, 1L);
        assertTrue(memorisedStudents.get(1L).equals(refreshingStudent));
    }
}