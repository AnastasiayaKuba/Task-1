package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import jm.task.core.jdbc.util.Util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    private SessionFactory sessionFactory;

    @Override
    public void createUsersTable() {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery(SQLQueries.SQLCreate).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Ошибка создания таблицы: " + e.getMessage());
        }
        log.info("Таблица пользователей успешно создана");
    }

    @Override
    public void dropUsersTable() {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery(SQLQueries.SQLDrop).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Ошибка удаления таблицы: " + e.getMessage());
        }
        log.info("Таблица пользователей успешно удалена!");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = new User();
            user.setName(name);
            user.setLastName(lastName);
            user.setAge(age);
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Ошибка при сохранении пользователя: {}", e.getMessage(), e);
        }
    }

    @Override
    public void removeUserById(long id) {
        User user = new User();
        user.setId(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Ошибка удаления пользователя");

        }
        log.info("Пользователь c ID {} удален!", id);

    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            log.error("Ошибка при получении всех пользователей: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public void cleanUsersTable() {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("delete  from User").executeUpdate();
            session.getTransaction().commit();
            log.info("Таблица очищена");
        }
    }
}
