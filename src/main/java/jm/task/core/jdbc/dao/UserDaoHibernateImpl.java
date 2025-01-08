package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoHibernateImpl.class);

    @Override
    public void createUsersTable() {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            session.createNativeQuery("CREATE TABLE IF NOT EXISTS users " +
                    "(id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "lastname VARCHAR(255), " +
                    "age SMALLINT)").executeUpdate();
            session.getTransaction().commit();
            log.info("Таблица создана.");
        } catch (Exception e) {
            log.error("Ошибка при создании таблицы 'users': {}", e.getMessage(), e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
            session.getTransaction().commit();
            log.info("Таблица удалена.");
        } catch (Exception e) {
            log.error("Ошибка при удалении таблицы: {}", e.getMessage(), e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            log.info("Пользователь {} {} успешно сохранён.", name, lastName);
        } catch (Exception e) {
            log.error("Ошибка при сохранении пользователя: {}", e.getMessage(), e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                session.getTransaction().commit();
                log.info("Пользователь с ID {} удалён.", id);
            } else {
                log.warn("Пользователь с ID {} не найден.", id);
                session.getTransaction().rollback();
            }
        } catch (Exception e) {
            log.error("Ошибка при удалении пользователя с ID {}: {}", id, e.getMessage(), e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            log.error("Ошибка при получении всех пользователей: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            session.getTransaction().commit();
            log.info("Таблица очищена.");
        } catch (Exception e) {
            log.error("Ошибка при очистке таблицы: {}", e.getMessage(), e);
        }
    }
}