package org.example.finalprojectmodule3.model;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Subject {
    PROGRAMMING("Программирование"),
    MATH("Математика"),
    PHYSICS("Физика"),
    PE("Физкультура"),
    HISTORY("История"),
    FOREIGN_LANGUAGE("Иностранный язык");

    private final String nameOfSubject;

    Subject(String nameOfSubject) {
        this.nameOfSubject = nameOfSubject;
    }

    public static Subject getByName(String subjectName) {
        for (Subject subject : values()) {
            if (subject.nameOfSubject.equals(subjectName)) {
                return subject;
            }
        }
        throw new IllegalArgumentException("Subject with name '" + subjectName + "' not found.");
    }

    @Override
    public String toString() {
        return nameOfSubject;
    }
}
