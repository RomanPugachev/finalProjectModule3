package org.example.finalprojectmodule3.service.validation;

import lombok.AllArgsConstructor;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.model.dto.StudentDTO;
import org.example.finalprojectmodule3.service.PropertyService;


import static org.example.finalprojectmodule3.consts.Consts.*;
import static org.example.finalprojectmodule3.service.validation.ValidationUtils.isNameCorrect;

@AllArgsConstructor
public class ValidationStudent {
    private MemoryDB memoryDB;
    private PropertyService propertyService;

    public void validateAddStudent(StudentDTO studentDTO) {
        String errorMessage = "";
        if (memoryDB.getStudents().containsKey(studentDTO.getId())) {
            errorMessage += "Студент с таким id уже существует.\n";
        }
        if (!isNameCorrect(studentDTO.getName()) || !isNameCorrect(studentDTO.getPatronymic()) || !isNameCorrect(studentDTO.getSurname())) {
            errorMessage += "Ошибка в ФИО студента, проверьте корректность введённых данных.\n";
        }
        if (!errorMessage.isBlank()) throw new RuntimeException(errorMessage);
    }

    public void validateDeleteStudentById(Long id){
        if(!memoryDB.getStudents().containsKey(id)) {
            throw new RuntimeException("Студент с таким id не обнаружен");
        }
    }

    public void validateUpdateStudentById(StudentDTO studentDTO, Long id) {
        String errorMessage = "";
        if (!memoryDB.getStudents().containsKey(id)) {
            errorMessage += "Студента с таким id не существует. Для добавления студента используйте POST запрос. ";
        }
        if (!isNameCorrect(studentDTO.getName()) || !isNameCorrect(studentDTO.getPatronymic()) || !isNameCorrect(studentDTO.getSurname())) {
            errorMessage += "Ошибка в ФИО студента, проверьте корректность введённых данных.";
        }
        if (errorMessage.length() > 0) throw new RuntimeException(String.format("Не удалось обновить студента с id %d\n%s", id, errorMessage));
    }
}