package org.example.finalprojectmodule3.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.model.dto.TeacherDTO;
import org.example.finalprojectmodule3.service.JsonService;
import org.example.finalprojectmodule3.service.PropertyService;
import org.example.finalprojectmodule3.service.TeacherService;
import org.example.finalprojectmodule3.service.validation.ValidationTeacher;

import java.io.IOException;

import static org.example.finalprojectmodule3.consts.Consts.*;
import static org.example.finalprojectmodule3.servlet.StudentServlet.getBody;

@WebServlet(SLASH + TEACHERS + SLASH + STAR)
public class TeacherServlet extends HttpServlet {
    private TeacherService teacherService;
    private JsonService jsonService;
    private ValidationTeacher validationTeacher;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        ServletContext context = config.getServletContext();
        this.teacherService = (TeacherService) context.getAttribute(TEACHER_SERVICE);
        this.jsonService = (JsonService) context.getAttribute(JSON_SERVICE);
        this.validationTeacher = new ValidationTeacher((MemoryDB) context.getAttribute(MEMORY_DB),
                (PropertyService) context.getAttribute(PROPERTY_SERVICE));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");

        resp.getWriter().println(teacherService.getAllTeachers().toString());
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");

        String teacherToSaveAsString = getBody(req);
        TeacherDTO teacherToSave = jsonService.fromJson(teacherToSaveAsString, TeacherDTO.class);
        try {
            validationTeacher.validateAddTeacher(teacherToSave);
            teacherService.addTeacher(teacherToSave);
            resp.setStatus(201);
            resp.getWriter().println("Учитель создан со следующими данными:\n" + teacherToSaveAsString);
        } catch (Exception e) {
            resp.setStatus(449);
            resp.getWriter().println(e.getMessage());
        }
    }

    @Override
     public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");

        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String pathInfo = requestURI.substring(contextPath.length());
        try {
            Long teacherId = Long.valueOf(pathInfo.split("/")[2]); // Извлечение индекса преподавателя
            String subjectName =(String) req.getParameter(SUBJECT);
            validationTeacher.validateAddTeacherSubject(teacherId, subjectName);
            teacherService.addTeacherSubject(teacherId, subjectName);
            resp.setStatus(201);
            resp.getWriter().printf("Преподавателю добавлен предмет %s", subjectName.toString());
        } catch (NumberFormatException e) {
            resp.setStatus(449);
            resp.getWriter().println("Введите корректное значение id преподавателя в адресе ресурса.");
        } catch (Exception e) {
            resp.setStatus(449);
            resp.getWriter().println(e.getMessage());
        }
    }
}
