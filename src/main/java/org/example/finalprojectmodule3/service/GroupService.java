package org.example.finalprojectmodule3.service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojectmodule3.model.Group;
import org.example.finalprojectmodule3.model.Student;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.model.dto.GroupDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GroupService {
    private final MemoryDB memoryDB;
    private final MappingService mappingService;

    public Collection<Group> getAllGroups(){
        return this.memoryDB.getGroups().values();
    }

    public Group getGroupByNumber(String groupNumber){
        return this.memoryDB.getGroups().values().stream()
                .filter(group -> group.getGroupNumber().equals(groupNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Не удалось найти группу с указанным номером"));
    }

    public Collection<Group> getGroupsByStudentSurname(String studentSurname){
        return this.memoryDB.getGroups().values().stream()
                .filter(group -> group.getStudents().stream()
                        .anyMatch(student -> student.getSurname().equals(studentSurname)))
                .collect(Collectors.toList());
    }

    public void addGroup(GroupDTO groupDTO){
        Map<Long, Group> groups = memoryDB.getGroups();
        Long appropriateId = memoryDB.getAppropriateKey(groups.keySet());
        List<Student> studentsForGroup = new ArrayList<>(groupDTO.getStudentsId().size());
        groupDTO.getStudentsId().forEach(studentId -> studentsForGroup.add(memoryDB.getStudents().get(studentId)));
        Group newGroup = mappingService.fromGroupDTO(groupDTO, appropriateId, studentsForGroup);
        groups.put(newGroup.getId(), newGroup);
    }

    public void addStudentIntoGroup(Long groupId, Long studentId){
        Student studentToAdd = memoryDB.getStudents().get(studentId);
        memoryDB.getGroups().get(groupId).getStudents().add(studentToAdd);
    }
}
