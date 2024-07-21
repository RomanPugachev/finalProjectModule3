package org.example.finalprojectmodule3.listener;


import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.finalprojectmodule3.model.data.MemoryDB;
import org.example.finalprojectmodule3.service.*;

import static org.example.finalprojectmodule3.consts.Consts.*;

@WebListener
public class Listener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        MemoryDB memoryDB = new MemoryDB();
        MappingService mappingService = new MappingService(memoryDB);
        PropertyService propertyService = new PropertyService();
        context.setAttribute(MEMORY_DB, memoryDB);
        context.setAttribute(PROPERTY_SERVICE, propertyService);
        context.setAttribute(JSON_SERVICE, new JsonService());
        context.setAttribute(STUDENT_SERVICE, new StudentService(memoryDB, mappingService));
        context.setAttribute(TEACHER_SERVICE, new TeacherService(memoryDB, mappingService));
        context.setAttribute(GROUP_SERVICE, new GroupService(memoryDB, mappingService));
        context.setAttribute(TIMETABLE_SERVICE, new TimetableService(memoryDB, mappingService));
    }
}
