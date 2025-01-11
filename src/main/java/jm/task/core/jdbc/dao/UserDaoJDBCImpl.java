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

    @Override
    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(SQLQueries.SQLCreate);
            log.info("Таблица 'users' успешно создана.");

        } catch (SQLException e) {
            log.error("Ошибка при создании таблицы 'users': {}", e.getMessage(), e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(SQLQueries.SQLDrop);
            log.info("Таблица 'users' успешно удалена.");

        } catch (SQLException e) {
            log.error("Ошибка при удалении таблицы 'users': {}", e.getMessage(), e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.SQLSave)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 1) {
                log.info("Пользователь {} {} успешно сохранён.", name, lastName);
            } else {
                log.warn("Не удалось сохранить пользователя {} {}.", name, lastName);
            }

        } catch (SQLException e) {
            log.error("Ошибка при сохранении пользователя {} {}: {}", name, lastName, e.getMessage(), e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.SQlRemove)) {

            preparedStatement.setLong(1, id);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                log.info("Пользователь с ID {} успешно удалён.", id);
            } else {
                log.warn("Пользователь с ID {} не найден.", id);
            }

        } catch (SQLException e) {
            log.error("Ошибка при удалении пользователя с ID {}: {}", id, e.getMessage(), e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLQueries.SQLGet);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String lastName = rs.getString("lastname");
                byte age = rs.getByte("age");

                User user = new User(name, lastName, age);
                user.setId(id);
                users.add(user);
            }

            log.info("Получен список всех пользователей: {} записей.", users.size());

        } catch (SQLException e) {
            log.error("Ошибка при получении всех пользователей: {}", e.getMessage(), e);
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(SQLQueries.SQLClean);
            log.info("Таблица 'users' успешно очищена.");

        } catch (SQLException e) {
            log.error("Ошибка при очистке таблицы 'users': {}", e.getMessage(), e);
        }
    }
}
