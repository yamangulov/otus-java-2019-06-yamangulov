package ru.otus;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.hibernate.SessionFactory;
import ru.otus.api.dao.EntityDao;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceEntity;
import ru.otus.api.service.DBServiceEntityImplCached;
import ru.otus.api.service.UserLoginService;
import ru.otus.filters.AuthorizationFilter;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.EntityDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.servlets.AdminServlet;
import ru.otus.servlets.LoginServlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Start {

    private static final int PORT = 8080;
    private static final String STATIC = "/static";

    public static void main(String[] args) throws Exception {
        Start start = new Start();
        Map<Long, EntityDao<User>> startedMap = start.initHibernate();
        start.initJetty(startedMap);
    }

    private void initJetty(Map<Long, EntityDao<User>> startedMap) throws Exception{
        ResourceHandler resourceHandler = new ResourceHandler();
        Resource resource = Resource.newClassPathResource(STATIC);
        resourceHandler.setBaseResource(resource);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new LoginServlet(userLoginService(startedMap))), "/login");
        context.addServlet(new ServletHolder(new AdminServlet(startedMap)), "/admin");
        context.addFilter(new FilterHolder(new AuthorizationFilter()), "/admin", null);

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();

    }

    private UserLoginService userLoginService(Map<Long, EntityDao<User>> startedMap) {
        return new UserLoginService(startedMap);
    }


    //так как везде у меня реализованы методы для получения сущности определенного класса по id, и нет реализованного
    //получения сущности из БД по имени (так было сделано в прошлых ДЗ), то я возвращаю созданного при инициализации
    //Hibernate первого пользователя в качестве админа, и передаю его далее в метод для инициализации Jetty, а Jetty уже
    //проверяет совпадение логина и пароля с кэшем или БД, где данный юзер хранится после создания

    //в ДЗ нет задачи авторизовываться под не-админами, но если бы была, то можно было бы реализовать метод для получения
    //сущности юзера из БД, проверки логина-пароля и авторизации под произвольным юзером. Но тогда потребовалась бы доп.
    //логика для маршрутизации юзера после авторизации с проверкой - админ он или не админ, доп. страницы для простого юзера
    // а это выходит за рамки ДЗ
    private Map<Long, EntityDao<User>> initHibernate() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, AddressDataSet.class, PhoneDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        EntityDao<User> entityDao = new EntityDaoHibernate<>(sessionManager);
        DBServiceEntity<User> dbServiceEntity = new DBServiceEntityImplCached<>(entityDao);

        AddressDataSet addressDataSet = new AddressDataSet("Lenina");
        List<PhoneDataSet> phoneDataSet = new ArrayList<>();
        phoneDataSet.add(new PhoneDataSet("555-555"));
        phoneDataSet.add(new PhoneDataSet("666-666"));

        User admin = new User( "admin", "111111", 50, addressDataSet, phoneDataSet);

        dbServiceEntity.createOrUpdateEntity(admin);
        long adminId = admin.getId();

        Map<Long, EntityDao<User>> resultMap = new HashMap<>();
        resultMap.put(adminId, entityDao);

        return resultMap;
    }

}
