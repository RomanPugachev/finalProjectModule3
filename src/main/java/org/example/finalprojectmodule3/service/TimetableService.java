package org.example.finalprojectmodule3.service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojectmodule3.model.Group;
import org.example.finalprojectmodule3.model.Teacher;
import org.example.finalprojectmodule3.model.TimetableElement;
import org.example.finalprojectmodule3.model.data.MemoryDB;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TimetableService {
    private final MemoryDB memoryDB;
    private final MappingService mappingService;

    public List<TimetableElement> getTimetable(){
        return memoryDB.getTimetable();
    }

    public List<TimetableElement> getTimetableForGroup(String groupNumber) {
        return memoryDB.getTimetable().stream()
                .filter(timetableElement -> {  // Фильтрация записей расписания, которые относятся к указанной группе
                    Group groupInTimetableElement = memoryDB.getGroups().get(timetableElement.getGroupId()); // Поиск группы по её id
                    if (groupInTimetableElement.getGroupNumber().equals(groupNumber)){
                        return true;
                    } else return false;
                })
                .toList();
    }

    public List<TimetableElement> getTimetableForStudent(String studentSurname){
        return memoryDB.getTimetable().stream()
                .filter(timetableElement -> {
                    Group groupInElement = memoryDB.getGroups().get(timetableElement.getGroupId());
                    if (groupInElement.getStudents().stream().anyMatch(student -> student.getSurname().equals(studentSurname))){
                        return true;
                    } else return false;
                })
                .toList();
    }

    public List<TimetableElement> getTimetableForTeacher(String teacherSurname){
        Map<Long, Teacher> teachers = memoryDB.getTeachers();
        Set<Long> teacherIds = teachers.keySet().stream()
                .filter(id -> teachers.get(id).getName().contains(teacherSurname))
                .collect(Collectors.toSet());
        return memoryDB.getTimetable().stream()
                .filter(timetableElement -> teacherIds.contains(timetableElement.getTeacherId()))
                .toList();
    }

    public List<TimetableElement> getTimetableForDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateToSearch = LocalDate.parse(dateString, formatter);
        return memoryDB.getTimetable().stream()
                .filter(timetableElement -> timetableElement.getStartDateTime().toLocalDate().equals(dateToSearch))
                .toList();
    }

    public void addTimetableElement(TimetableElement timetableElementToAdd) {
        memoryDB.getTimetable().add(timetableElementToAdd);
    }

    public void refreshTimetableElement(List<TimetableElement> timetableElementsToAdd, LocalDate date) {
        List<TimetableElement> timetable = memoryDB.getTimetable();
        timetable.removeIf(timetableElement -> timetableElement.getStartDateTime().toLocalDate().equals(date));
        timetable.addAll(timetableElementsToAdd);
    }
}
