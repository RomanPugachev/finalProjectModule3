package org.example.finalprojectmodule3.service.validation;

import lombok.AllArgsConstructor;
import org.example.finalprojectmodule3.model.Subject;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.model.dto.TeacherDTO;
import org.example.finalprojectmodule3.service.PropertyService;


import static org.example.finalprojectmodule3.service.validation.ValidationUtils.isNameCorrect;

@AllArgsConstructor
public class ValidationTeacher {
    private MemoryDB memoryDB;
    private PropertyService propertyService;

    public void validateAddTeacher(TeacherDTO teacherToSave) {
        String errorMessage = "";
        if (memoryDB.getTeachers().containsKey(teacherToSave.getId())){
            errorMessage += "Учитель с таким id уже существует.\n";
        }
        if (!isNameCorrect(teacherToSave.getName())){
            errorMessage += "Введено недопустимое имя учителя. Проверьте корректность введённых данных.\n";
        }
        if (teacherToSave.getSubjects().size() < Integer.parseInt(propertyService.getProperties().getProperty("min.teacherSubjects"))) {
            errorMessage += "Создаваемый преподаватель владеет недостаточным количеством дисциплин.\n";
        }
        if (!errorMessage.isBlank()) throw new RuntimeException(errorMessage);
    }

    public void validateAddTeacherSubject(Long teacherId, String subjectName) {
        String errorMessage = "";
        if (!memoryDB.getTeachers().containsKey(teacherId)){
            errorMessage += "Учителя с таким id не существует";
        }
        try {
            Subject.getByName(subjectName);
        } catch (Exception e) {
            errorMessage += e.getMessage();
        }
        if (!errorMessage.isBlank()) throw new RuntimeException(errorMessage);
    }
}
