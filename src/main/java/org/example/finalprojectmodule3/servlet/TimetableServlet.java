package org.example.finalprojectmodule3.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.finalprojectmodule3.model.TimetableElement;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.service.JsonService;
import org.example.finalprojectmodule3.service.PropertyService;
import org.example.finalprojectmodule3.service.TimetableService;
import org.example.finalprojectmodule3.service.validation.ValidationStudent;
import org.example.finalprojectmodule3.service.validation.ValidationTimetable;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.example.finalprojectmodule3.consts.Consts.*;

@WebServlet(SLASH + TIMETABLE + SLASH + STAR)
public class TimetableServlet extends HttpServlet {
    private JsonService jsonService;
    private TimetableService timetableService;
    private ValidationTimetable validationTimetable;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        jsonService = (JsonService) context.getAttribute(JSON_SERVICE);
        timetableService = (TimetableService) context.getAttribute(TIMETABLE_SERVICE);
        validationTimetable = new ValidationTimetable((MemoryDB) context.getAttribute(MEMORY_DB),
                (PropertyService) context.getAttribute(PROPERTY_SERVICE));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");

        String groupNumber = req.getParameter(GROUP_NUMBER);
        if (groupNumber != null){
            try {
                resp.getWriter().println(timetableService.getTimetableForGroup(groupNumber).toString());
                return;
            } catch (Exception e) {
                resp.setStatus(449);
                resp.getWriter().println(e.getMessage());
                return;
            }
        }

        String studentSurname = req.getParameter(STUDENT_SURNAME);
        if (studentSurname != null){
            try {
                resp.getWriter().println(timetableService.getTimetableForStudent(studentSurname).toString());
                return;
            } catch (Exception e) {
                resp.setStatus(449);
                resp.getWriter().println(e.getMessage());
                return;
            }
        }

        String teacherSurname = req.getParameter(TEACHER_SURNAME);
        if (teacherSurname != null){
            try {
                resp.getWriter().println(timetableService.getTimetableForTeacher(teacherSurname).toString());
                return;
            } catch (Exception e) {
                resp.setStatus(449);
                resp.getWriter().println(e.getMessage());
                return;
            }
        }

        String dateString = req.getParameter(DATE);
        if (dateString != null) {
            try {
                resp.getWriter().println(timetableService.getTimetableForDate(dateString).toString());
                return;
            } catch (IOException e) {
                resp.setStatus(449);
                resp.getWriter().println(e.getMessage());
            }
        }
        resp.getWriter().println(timetableService.getTimetable().toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");

        String reqBody = StudentServlet.getBody(req);
        try {
            TimetableElement timetableElementToAdd = jsonService.fromJson(reqBody, TimetableElement.class);
            validationTimetable.validateAddTimetableElement(timetableElementToAdd);
            timetableService.addTimetableElement(timetableElementToAdd);
        } catch (Exception e) {
            resp.getWriter().println(e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");

        String reqBody = StudentServlet.getBody(req);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(req.getParameter(DATE), formatter);
            ObjectMapper objectMapper = jsonService.getObjectMapper();
            List<TimetableElement> timetableElementsToAdd = objectMapper.readValue(reqBody, objectMapper.getTypeFactory().constructCollectionType(List.class, TimetableElement.class));
            resp.getWriter().println(date.toString());
            validationTimetable.validateRefreshTimetableElements(timetableElementsToAdd, date);
            timetableService.refreshTimetableElement(timetableElementsToAdd, date);
            resp.getWriter().println("Расписание на указанную дату успешно обновлено.");
        } catch (Exception e) {
            resp.getWriter().println(e.getMessage());
        }
    }
}
