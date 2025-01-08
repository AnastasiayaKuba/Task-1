package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        String sql = SqlQueryProvider.getQuery("create.table.users");
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            log.info("Таблица users успешно создана");

        } catch (SQLException e) {
            log.error("Ошибка при создании таблицы users: " + e.getMessage(), e);
        }
    }

    public void dropUsersTable() {
        String sql = SqlQueryProvider.getQuery("drop.table.users");

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);
            log.info("Таблица «пользователи» успешно удалена.");

        } catch (SQLException e) {
            log.error("Ошибка при удалении таблицы «пользователи»: " + e.getMessage(), e);
            throw new RuntimeException("Не удалось удалить таблицу «пользователи»", e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = SqlQueryProvider.getQuery("insert.user");

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 1) {
                log.info("Пользователь с именем {} {} успешно сохранён.", name, lastName);
            } else if (affectedRows == 0) {
                log.warn("Не удалось сохранить пользователя с именем {} {}.", name, lastName);
            } else {
                log.error("Неожиданное количество затронутых строк: {}. Ожидалось 1.", affectedRows);
                throw new IllegalStateException("Нарушена целостность данных: затронуто несколько строк вместо одной.");
            }

        } catch (SQLException e) {
            log.error("Ошибка при сохранении пользователя с именем {} {}: {}", name, lastName, e.getMessage(), e);
            throw new RuntimeException("Не удалось сохранить пользователя", e);
        }
    }

    public void removeUserById(long id) {
        String sql = SqlQueryProvider.getQuery("delete.user.by.id");
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                log.info("Пользователь с ID {} успешно удалён.", id);
            } else {
                log.warn("Пользователь с ID {} не найден.", id);
            }

        } catch (SQLException e) {
            log.error("Ошибка при удалении пользователя с ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Не удалось удалить пользователя с ID " + id, e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = SqlQueryProvider.getQuery("select.all.users");

        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String lastName = rs.getString("lastName");
                byte age = rs.getByte("age");
                User user = new User(name, lastName, age);
                user.setId(id);
                users.add(user);
            }
            log.info("Получен список всех пользователей: {} записей.", users.size());
        } catch (SQLException e) {
            log.error("Ошибка при получении всех пользователей: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось получить список пользователей", e);
        }
        return users;
    }

    public void cleanUsersTable() {
        String sql = SqlQueryProvider.getQuery("truncate.users.table");

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.execute();
            log.info("Таблица 'users' была успешно очищена.");

        } catch (SQLException e) {
            log.error("Ошибка при очистке таблицы 'users': {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось очистить таблицу 'users'", e);
        }
    }
}
