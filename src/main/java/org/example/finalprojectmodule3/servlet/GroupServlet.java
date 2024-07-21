package org.example.finalprojectmodule3.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.model.dto.GroupDTO;
import org.example.finalprojectmodule3.service.GroupService;
import org.example.finalprojectmodule3.service.JsonService;
import org.example.finalprojectmodule3.service.PropertyService;
import org.example.finalprojectmodule3.service.validation.ValidationGroup;

import java.io.IOException;
import java.io.PrintWriter;

import static org.example.finalprojectmodule3.consts.Consts.*;

@WebServlet(SLASH + GROUPS + SLASH + STAR)
public class GroupServlet extends HttpServlet {
    private GroupService groupService;
    private JsonService jsonService;
    private ValidationGroup validationGroup;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        groupService = (GroupService) context.getAttribute(GROUP_SERVICE);
        jsonService = (JsonService) context.getAttribute(JSON_SERVICE);
        validationGroup = new ValidationGroup((MemoryDB) context.getAttribute(MEMORY_DB),
                (PropertyService) context.getAttribute(PROPERTY_SERVICE));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");
        if (req.getParameterMap().isEmpty()) {
            resp.getWriter().println(groupService.getAllGroups().toString());
            return;
        }
        String studentSurname = req.getParameter(SURNAME);
        if (studentSurname != null){
            resp.getWriter().println(groupService.getGroupsByStudentSurname(studentSurname));
            return;
        }
        String groupNumber = req.getParameter(GROUP_NUMBER);
        if (groupNumber != null){
            resp.getWriter().println(groupService.getGroupByNumber(groupNumber));
            return;
        }
        resp.setStatus(404);
        resp.getWriter().printf("Неверная комбинация параметров запроса к ресурсу groups.\n" +
                "Попробуйте отправить запрос с параметрами %s или groupNumber", SURNAME);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String groupToSaveString = StudentServlet.getBody(req);
        GroupDTO groupDTO = jsonService.fromJson(groupToSaveString, GroupDTO.class);
        try{
            validationGroup.validateGroupCreatingDTO(groupDTO);
            groupService.addGroup(groupDTO);
            resp.setStatus(201);
            resp.getWriter().println("Группа успешно создана");
        } catch (Exception e) {
            resp.setStatus(449);
            resp.getWriter().println(e.getMessage());
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");
        String requestURI = req.getRequestURI();
        try {
            Long studentId = Long.valueOf(req.getParameter(STUDENT_ID));
            Long groupId = Long.valueOf(requestURI.split("/")[3]);
            validationGroup.validateAddStudentIntoGroup(groupId, studentId);
            groupService.addStudentIntoGroup(groupId, studentId);
        } catch (Exception e) {
            resp.setStatus(449);
            PrintWriter respWriter = resp.getWriter();
            respWriter.println("Возникла ошибка:\n" + e.getMessage());
            resp.getWriter().printf("Попробуйте повторно отправить корректный PUT-запрос на ресурс %s?studentId={idOfSomeStudent}", requestURI);
        }
    }
}
