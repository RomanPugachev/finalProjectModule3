package org.example.finalprojectmodule3.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.model.dto.StudentDTO;
import org.example.finalprojectmodule3.service.JsonService;
import org.example.finalprojectmodule3.service.PropertyService;
import org.example.finalprojectmodule3.service.StudentService;
import org.example.finalprojectmodule3.service.validation.ValidationStudent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.example.finalprojectmodule3.consts.Consts.*;

@WebServlet(SLASH + STUDENTS + SLASH + STAR)
public class StudentServlet extends HttpServlet {
    private StudentService studentService;
    private JsonService jsonService;
    private ValidationStudent validationStudent;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        ServletContext context = config.getServletContext();
        this.studentService = (StudentService) context.getAttribute(STUDENT_SERVICE);
        this.jsonService = (JsonService) context.getAttribute(JSON_SERVICE);
        this.validationStudent = new ValidationStudent((MemoryDB) context.getAttribute(MEMORY_DB),
                (PropertyService) context.getAttribute(PROPERTY_SERVICE));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");

        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String pathInfo = requestURI.substring(contextPath.length());
        String surname = req.getParameter(SURNAME);
        if (surname != null){ //Обработка запроса студента по его фамилии
            resp.getWriter().println(studentService.getStudentsBySurname(surname));
            return;
        }
        try {//Обработка запроса студента по его id
            String idInfo = pathInfo.split("/")[2];
            Long id = Long.valueOf(idInfo);
            resp.getWriter().println(studentService.getStudentById(id));
            return;
        } catch (Throwable e){}
        // Если не какой то из Get-запросов с условиями выше, то возвращается весь список студентов
        resp.getWriter().println(studentService.getAllStudents().toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String studentToSaveAsString = getBody(req);
        try {
            StudentDTO studentToSave = jsonService.fromJson(studentToSaveAsString, StudentDTO.class);
            validationStudent.validateAddStudent(studentToSave);
            studentService.addStudent(studentToSave);
            resp.setStatus(201);
            resp.getWriter().println("Студент создан со следующими данными:\n" + studentToSaveAsString);
        } catch (Exception e) {
            resp.setStatus(449);
            resp.getWriter().println(e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long id = Long.valueOf(req.getParameter(ID));
            validationStudent.validateDeleteStudentById(id);
            studentService.deleteStudentById(Long.valueOf(id));
            resp.setStatus(200);
            resp.getWriter().printf("Удаление студента с индексом %s произведено успешно", id);
        } catch (Exception e) {
            resp.setStatus(449);
            resp.getWriter().println("Не удалось удалить студента с указанным id");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String updatingStudentString = getBody(req);
        StudentDTO updatingStudentDTO = jsonService.fromJson(updatingStudentString, StudentDTO.class);
        try {
            Long id = Long.valueOf(req.getParameter(ID));
            validationStudent.validateUpdateStudentById(updatingStudentDTO, id);
            studentService.updateStudentById(updatingStudentDTO, id);
            resp.setStatus(201);
        } catch (Exception e) {
            resp.setStatus(449);
            resp.getWriter().println(e.getMessage());
        }
    }

    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }
}
