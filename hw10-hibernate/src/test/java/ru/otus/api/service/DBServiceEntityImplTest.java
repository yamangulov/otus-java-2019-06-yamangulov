package ru.otus.api.service;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.api.dao.EntityDao;
import ru.otus.api.model.AddressDataSet;
import ru.otus.api.model.PhoneDataSet;
import ru.otus.api.model.User;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.EntityDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Сервис для работы с пользователями в рамках БД должен ")
@ExtendWith(MockitoExtension.class)
class DBServiceEntityImplTest {

    private static final long USER_ID = 1L;

    private SessionFactory sessionFactory;

    @Mock
    private SessionManagerHibernate sessionManager;

    @Mock
    private EntityDao<User> entityDao;

    private DBServiceEntity<User> dbServiceEntity;

    private User user;

    @BeforeEach
    void setUp() {
        sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, AddressDataSet.class, PhoneDataSet.class);
        sessionManager = new SessionManagerHibernate(sessionFactory);
        entityDao = new EntityDaoHibernate<>(sessionManager);
        dbServiceEntity = new DBServiceEntityImpl<>(entityDao);

        user = new User();
    }

    @Test
    @DisplayName(" корректно сохранять пользователя")
    void shouldCorrectSaveUser() {
        entityDao.createOrUpdate(user);
        assertThat(entityDao.getResultId()).isEqualTo(USER_ID);

        dbServiceEntity.createOrUpdateEntity(user);
        long id = dbServiceEntity.getResultId();
        assertThat(id).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName(" корректно загружать пользователя по заданному id")
    void shouldLoadCorrectUserById() {
        AddressDataSet addressDataSet = new AddressDataSet("Lenina");
        List<PhoneDataSet> phoneDataSet = new ArrayList<>();
        phoneDataSet.add(new PhoneDataSet("555-555"));
        phoneDataSet.add(new PhoneDataSet("666-666"));
        User expectedUser = new User( "Вася", 55, addressDataSet, phoneDataSet);
        dbServiceEntity.createOrUpdateEntity(expectedUser);
        User mayBeUser = dbServiceEntity.getEntity(USER_ID, User.class);
        //address, phoneList отдаются как разные объекты, поэтому по ним не сравниваю для упрощения, иначе пришлось бы усложнить алгоритм сравнения
        assertThat(mayBeUser).isEqualToIgnoringGivenFields(expectedUser, "id", "address", "phoneList");
    }

}

@ExtendWith(MockitoExtension.class)
class DBServiceEntityImplTestOrder {
    @Mock
    private SessionManagerHibernate sessionManager;

    @Mock
    private EntityDao<User> entityDao;

    private DBServiceEntity<User> dbServiceEntity;

    private User user;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        given(entityDao.getSessionManager()).willReturn(sessionManager);
        inOrder = inOrder(entityDao, sessionManager);
        dbServiceEntity = new DBServiceEntityImpl<>(entityDao);
        user = new User();
    }

    @Test
    @DisplayName(" при сохранении пользователя, открывать и коммитить транзакцию в нужном порядке")
    void shouldCorrectSaveUserAndOpenAndCommitTranInExpectedOrder() {
        dbServiceEntity.createOrUpdateEntity(user);

        inOrder.verify(entityDao, times(1)).getSessionManager();
        inOrder.verify(sessionManager, times(1)).beginSession();
        inOrder.verify(sessionManager, times(1)).commitSession();
        inOrder.verify(sessionManager, never()).rollbackSession();
    }

    @Test
    @DisplayName(" при сохранении пользователя, открывать и откатывать транзакцию в нужном порядке")
    void shouldOpenAndRollbackTranWhenExceptionInExpectedOrder() {

        doThrow(IllegalArgumentException.class).when(entityDao).createOrUpdate(any());

        assertThatThrownBy(() -> dbServiceEntity.createOrUpdateEntity(null))
                .isInstanceOf(DBServiceException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);


        inOrder.verify(entityDao, times(1)).getSessionManager();
        inOrder.verify(sessionManager, times(1)).beginSession();
        inOrder.verify(sessionManager, times(1)).rollbackSession();
        inOrder.verify(sessionManager, never()).commitSession();
    }
}