package ru.otus.servlets;

import ru.otus.api.service.UserLoginService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final String AUTH_RESULT_VARIABLE_NAME = "resultMessage";
    private static final String AUTH_RESULT_VARIABLE_VALUE = "Authorization failed! Check your login/password.";
    private final TemplateProcessor templateProcessor;
    private static final int EXPIRE_INTERVAL = 200;
    private final UserLoginService userLoginService;

    public LoginServlet(UserLoginService userLoginService) throws IOException{
        this.userLoginService = userLoginService;
        this.templateProcessor = new TemplateProcessor();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(AUTH_RESULT_VARIABLE_NAME, "");

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter("username");
        String password = request.getParameter("password");

        if (userLoginService.authenticate(name, password)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(300);
            response.sendRedirect("/admin");
        } else {
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put(AUTH_RESULT_VARIABLE_NAME, AUTH_RESULT_VARIABLE_VALUE);

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}
