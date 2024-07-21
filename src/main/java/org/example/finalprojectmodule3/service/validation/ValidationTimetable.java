package org.example.finalprojectmodule3.service.validation;

import lombok.AllArgsConstructor;
import org.example.finalprojectmodule3.model.TimetableElement;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.service.PropertyService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.example.finalprojectmodule3.service.validation.ValidationUtils.periodsIntersect;

@AllArgsConstructor
public class ValidationTimetable {
    private MemoryDB memoryDB;
    private PropertyService propertyService;

    public void validateAddTimetableElement(TimetableElement timetableElementToAdd) {
        if (timetableElementToAdd.getStartDateTime().isAfter(timetableElementToAdd.getEndDateTime())){
            LocalDateTime temp = timetableElementToAdd.getEndDateTime();
            timetableElementToAdd.setEndDateTime(timetableElementToAdd.getStartDateTime());
            timetableElementToAdd.setStartDateTime(temp);
        }
        boolean groupExists = memoryDB.getGroups().containsKey(timetableElementToAdd.getGroupId());
        boolean teacherExists = memoryDB.getTeachers().containsKey(timetableElementToAdd.getTeacherId());
        boolean groupIsBusy = memoryDB.getTimetable().stream()
                .anyMatch(timetableElement -> timetableElement.getGroupId().equals(timetableElementToAdd.getGroupId())
                        && periodsIntersect(timetableElement.getStartDateTime(), timetableElement.getEndDateTime(), timetableElementToAdd.getStartDateTime(), timetableElementToAdd.getEndDateTime()));
        boolean teacherIsBusy = memoryDB.getTimetable().stream()
                .anyMatch(timetableElement -> timetableElement.getTeacherId().equals(timetableElementToAdd.getTeacherId())
                        && periodsIntersect(timetableElement.getStartDateTime(), timetableElement.getEndDateTime(), timetableElementToAdd.getStartDateTime(), timetableElementToAdd.getEndDateTime()));
        if (teacherIsBusy || groupIsBusy || !teacherExists || !groupExists) {
            String errorMessage = !groupExists ? "Указанной группы не существует" :
                    !teacherExists ? "Указанного учителя не существует" :
                            groupIsBusy ? "Группа уже имеет другое занятие в это время. Попробуйте указать другое время" :
                                    teacherIsBusy ? "Учитель уже имеет другое занятие в это время. Попробуйте указать другое время" : "Неизвестная ошибка";
            throw new RuntimeException(errorMessage);
        }
    }

    public void validateRefreshTimetableElements(List<TimetableElement> newTimetableElements, LocalDate date) {
        String errorMessage = "";
        if (newTimetableElements.stream().anyMatch(timetableElement ->
                !timetableElement.getStartDateTime().toLocalDate().equals(timetableElement.getEndDateTime().toLocalDate()) ||
                !timetableElement.getStartDateTime().toLocalDate().equals(date) ||
                !timetableElement.getStartDateTime().isBefore(timetableElement.getEndDateTime()))) {
            errorMessage += "Введена некорректная дата.\n";
        }
        Integer minClasses = Integer.valueOf(propertyService.getProperties().getProperty("min.classes"));
        Integer maxClasses = Integer.valueOf(propertyService.getProperties().getProperty("max.classes"));
        if (memoryDB.getGroups().keySet().stream().anyMatch(groupId -> {
                long classesPerDay = newTimetableElements.stream().filter(timetableElement -> timetableElement.getGroupId().equals(groupId)).count();
                return classesPerDay > maxClasses || classesPerDay < minClasses;
        })) {
            errorMessage += String.format("Проверьте занятость студенческих групп: каждая группа должна иметь как минимум %d занятий и как максимум %d занятий\n", minClasses, maxClasses);
        }
        if (newTimetableElements.stream().anyMatch(timetableElement -> !memoryDB.getGroups().containsKey(timetableElement.getGroupId()))) {
            errorMessage += "Указана неизвестная группа.\n";
        }
        if (newTimetableElements.stream().anyMatch(timetableElement -> !memoryDB.getTeachers().containsKey(timetableElement.getTeacherId()))) {
            errorMessage += "Указан неизвестный преподаватель.\n";
        }
        if (!errorMessage.isBlank()) throw new RuntimeException(errorMessage);
    }
}
