package org.example.finalprojectmodule3.model;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

import static org.example.finalprojectmodule3.consts.Consts.*;

@Data
public class Group {
    private Long id;
    private String groupNumber;
    private List<Student> students;

    private Group(Long id, String groupNumber, List<Student> students){
        this.id = id;
        this.groupNumber = groupNumber;
        this.students = students;
    }

    public void addStudent(Student newStudent){
        if (this.students.contains(newStudent)){
            throw new RuntimeException("Этот студент уже находится в текущей группе");
        }
        this.students.add(newStudent);
    }

    public static GroupBuilder builder(){
        return new GroupBuilder();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append(ID, id)
                .append(GROUP_NUMBER, groupNumber)
                .append(STUDENTS, students)
                .toString();
    }

    public static class GroupBuilder {
        private Long idToSet = null;
        private String groupNumberToSet = null;
        private List<Student> studentsToSet = new ArrayList<>();

        public GroupBuilder id(Long idToSet){
            this.idToSet = idToSet;
            return this;
        }

        public GroupBuilder groupNumber(String groupNumberToSet){
            this.groupNumberToSet = groupNumberToSet;
            return this;
        }

        public GroupBuilder students(List<Student> students) {
            this.studentsToSet = students;
            return this;
        }

        public GroupBuilder student(Student newStudent){
            if (this.studentsToSet.contains(newStudent)){
                return this;
            }
            this.studentsToSet.add(newStudent);
            return this;
        }

        public Group build(){
            return new Group(idToSet, groupNumberToSet, studentsToSet);
        }
    }
}