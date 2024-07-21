package org.example.finalprojectmodule3.model.data;

import java.time.LocalDateTime;
import java.util.*;

import lombok.Data;
import org.example.finalprojectmodule3.model.*;


@Data
public class MemoryDB {
    private Map<Long, Student> students;
    private Map<Long, Teacher> teachers;
    private Map<Long, Group> groups;
    private List<TimetableElement> timetable;

    public MemoryDB(){
        students = new HashMap<>();
        students.put(0L, Student.builder().id(0L).build());
        students.put(1L, Student.builder().id(1L).name("Petr").patronymic("Petrovich").surname("Petrov").build());
        teachers = new HashMap<>();
        teachers.put(0L, Teacher.builder().id(0L).subject(Subject.MATH).build());
        teachers.put(1L, Teacher.builder().id(1L).name("Антон").subject(Subject.PROGRAMMING).build());
        teachers.put(2L, Teacher.builder().id(2L).name("Алексей").subject(Subject.FOREIGN_LANGUAGE).build());
        groups = new HashMap<>();
        groups.put(0L, Group.builder().id(0L).groupNumber("2").students(students.values().stream().toList()).build());
        timetable = new ArrayList<>(10);
        timetable.add(TimetableElement.builder()
                .groupId(0L)
                .teacherId(0L)
                .startDateTime(LocalDateTime.of(2024, 5, 22, 9, 0))
                .endDateTime(LocalDateTime.of(2024, 5, 22, 10, 25))
                .build());
        timetable.add(TimetableElement.builder()
                .groupId(0L)
                .teacherId(1L)
                .startDateTime(LocalDateTime.of(2024, 5, 22, 10, 35))
                .endDateTime(LocalDateTime.of(2024, 5, 22, 12, 0))
                .build());
    }
    public Long getAppropriateKey(Set<Long> keySet){
        if (keySet.isEmpty()){
            return 0L;
        }
        for (int i = 0; i < keySet.size() + 1; i++){
            if (!keySet.contains(Long.valueOf(i))) {
                return Long.valueOf(i);
            }
        }
        throw new RuntimeException();
    }
}
