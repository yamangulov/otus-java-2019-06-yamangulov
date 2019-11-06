package ru.otus.servlets;

import ru.otus.api.dao.EntityDao;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;
import ru.otus.api.service.DBServiceEntityImplCached;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class AdminServlet extends HttpServlet {
    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";
    private static final String CREATED_USER_VAR_NAME = "createdUser";
    private static final String USERS_LIST_VAR_NAME = "usersList";

    private final TemplateProcessor templateProcessor;
    private DBServiceEntity<User> dbServiceEntity;


    public AdminServlet(EntityDao<User> entityDao) throws IOException {
        this.templateProcessor = new TemplateProcessor();
        this.dbServiceEntity = new DBServiceEntityImplCached<>(entityDao);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(USERS_LIST_VAR_NAME, Collections.emptyList());
        pageVariables.put(CREATED_USER_VAR_NAME, Collections.emptyList());

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // создать нового пользователя
        if (request.getParameter("createUser") != null) {
            String userName = request.getParameter("userName");
            int userAge = Integer.parseInt(request.getParameter("userAge"));
            AddressDataSet addressDataSet = new AddressDataSet("");
            List<PhoneDataSet> phoneDataSet = new ArrayList<>();
            phoneDataSet.add(new PhoneDataSet(""));
            phoneDataSet.add(new PhoneDataSet(""));

            User savedUser = new User(userName, "", userAge, addressDataSet, phoneDataSet);

            dbServiceEntity.createOrUpdateEntity(savedUser);

            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put(CREATED_USER_VAR_NAME, Collections.singletonList(savedUser));
            pageVariables.put(USERS_LIST_VAR_NAME, Collections.emptyList());

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, pageVariables));
            response.setStatus(HttpServletResponse.SC_OK);

        } else if (request.getParameter("getUsersList") != null) { // получить список пользователей
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put(CREATED_USER_VAR_NAME, Collections.emptyList());
            pageVariables.put(USERS_LIST_VAR_NAME, dbServiceEntity.getUsersList());

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, pageVariables));
            response.setStatus(HttpServletResponse.SC_OK);
        } else if (request.getParameter("exitAdminPanel") != null) { //выйти из панели администратора
            request.getSession().invalidate();
            response.sendRedirect("/login");
        }

    }
}
