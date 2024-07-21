package org.example.finalprojectmodule3.service.validation;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

import static org.example.finalprojectmodule3.consts.Consts.DEPRECATED_SYMBOLS;

public class ValidationUtils {
    public static boolean periodsIntersect(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        if (start1.isBefore(start2)) {
            if (end1.isBefore(start2)) {
                return false;
            } else return true;
        } else if (start1.isBefore(end2)) {
            return true;
        } else return false;
    }

    public static boolean isNameCorrect(String someName) {
        if (StringUtils.containsAny(someName, DEPRECATED_SYMBOLS) || StringUtils.isBlank(someName)){
            return false;
        }
        return true;
    }
}
