package org.example.finalprojectmodule3.service.validation;

import lombok.AllArgsConstructor;
import org.example.finalprojectmodule3.model.Group;
import org.example.finalprojectmodule3.model.Student;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.model.dto.GroupDTO;
import org.example.finalprojectmodule3.service.PropertyService;

import java.util.Map;

@AllArgsConstructor
public class ValidationGroup {
    private MemoryDB memoryDB;
    private PropertyService propertyService;

    public void validateGroupCreatingDTO(GroupDTO groupDTO){
        Map<Long, Group> groups = memoryDB.getGroups();
        String errorMessage = "";
        if(groups.values().stream()
                .anyMatch(group -> group.getGroupNumber().equals(groupDTO.getGroupNumber()))){
            errorMessage += "Группа с таким номером уже существует.\n";
        }
        if(!(groupDTO.getStudentsId().stream()
                .allMatch(studentId -> memoryDB.getStudents().containsKey(studentId)))){
            errorMessage += "Студента с одним из указанных id не существует.\n";
        }
        if (groupDTO.getStudentsId().size() < Integer.parseInt(propertyService.getProperties().getProperty("min.studentsInGroup"))){
            errorMessage += "В добавляемой группе недостаточно студентов.\n";
        }
        if (groupDTO.getStudentsId().size() > Integer.parseInt(propertyService.getProperties().getProperty("max.studentsInGroup"))){
            errorMessage += "В добавляемой группе слишком много студентов.\n";
        }
        if (!errorMessage.isBlank()) throw new RuntimeException(errorMessage);
    }

    public void validateAddStudentIntoGroup(Long groupId, Long studentId){
        if (!memoryDB.getGroups().containsKey(groupId)){
            throw new RuntimeException("Не удалось найти группу с указанным id");
        }
        if (!memoryDB.getStudents().containsKey(studentId)) {
            throw new RuntimeException("Не удалось добавить студента в группу: отсутствует студент с таким id");
        }
        Student studentToAdd = memoryDB.getStudents().get(studentId);
        if (memoryDB.getGroups().get(groupId).getStudents().stream().anyMatch(student -> student.equals(studentToAdd))){
            throw new RuntimeException("Не удалось добавить студента в группу: в указанной группе уже существует такой студент");
        }
    }
}
