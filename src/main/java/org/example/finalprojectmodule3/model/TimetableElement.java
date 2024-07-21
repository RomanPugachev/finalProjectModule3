package org.example.finalprojectmodule3.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.example.finalprojectmodule3.consts.Consts.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public  class TimetableElement {
    private Long groupId;
    private Long teacherId;
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime startDateTime;
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime endDateTime;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN); // DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append(GROUP_ID, groupId)
                .append(TEACHER_ID, teacherId)
                .append(START_DATE_TIME, startDateTime.format(formatter))
                .append(END_DATE_TIME, endDateTime.format(formatter))
                .toString();
    }
}