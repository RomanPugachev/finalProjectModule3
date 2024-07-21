package org.example.finalprojectmodule3.model;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.example.finalprojectmodule3.consts.Consts.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    private Long id;
    private String name;
    private Long experience;
    private ArrayList<Subject> subjects = new ArrayList<>();

    public void addSubject(Subject newSubject){
        if (this.subjects.contains(newSubject)){
            throw new RuntimeException("Преподаватель уже преподаёт этот предмет");
        }
        this.subjects.add(newSubject);
    }

    public static TeacherBuilder builder(){
        return new TeacherBuilder();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append(ID, id)
                .append(NAME, name)
                .append(SUBJECTS, subjects.stream().map(Subject::toString).toArray(String[]::new))
                .append(EXPERIENCE, experience)
                .toString();
    }

    public static class TeacherBuilder {
        private Long idToSet = null;
        private String nameToSet = "Ivan";
        private Long experienceToSet = 30L;
        private ArrayList<Subject> subjectsToSet = new ArrayList<>();

        public TeacherBuilder id(Long idToSet){
            this.idToSet = idToSet;
            return this;
        }

        public TeacherBuilder name(String nameToSet){
            this.nameToSet = nameToSet;
            return this;
        }

        public TeacherBuilder experience(Long experienceToSet){
            this.experienceToSet = experienceToSet;
            return this;
        }

        public TeacherBuilder subject(Subject newSubject){
            if (this.subjectsToSet.contains(newSubject)){
                return this;
            }
            this.subjectsToSet.add(newSubject);
            return this;
        }

        public TeacherBuilder subjects(ArrayList<Subject> subjects){
            this.subjectsToSet = subjects;
            return this;
        }

        public Teacher build(){
            return new Teacher(idToSet, nameToSet, experienceToSet, subjectsToSet);
        }
    }
}