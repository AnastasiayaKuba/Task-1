package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

@Slf4j
public class Util {
    private static final String PROPERTIES_FILE_JDBC = "jdbc.properties";
    private static String url;
    private static String username;
    private static String password;
    private static String driver;
    private static Connection connection;

    static {
        try (InputStream inputStream = Util.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_JDBC)) {
            if (inputStream == null) {
                System.err.println("Файл свойств не найден: " + PROPERTIES_FILE_JDBC);
                throw new RuntimeException("Файл свойств " + PROPERTIES_FILE_JDBC + " не найден в classpath");
            }

            Properties properties = new Properties();
            properties.load(inputStream);

            url = properties.getProperty("jdbc.url");
            username = properties.getProperty("jdbc.username");
            password = properties.getProperty("jdbc.password");
            driver = properties.getProperty("jdbc.driver");

            System.out.println("URL: " + url);
            System.out.println("Имя пользователя: " + username);

            Class.forName(driver);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка загрузки свойств регистрации БД.");
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поключения к БД");
        }
        return connection;
    }

    public static Session getSession() {
        Session session = null;
        try {
            Configuration configuration = new Configuration().addAnnotatedClass(User.class);
            SessionFactory sessionFactory = configuration.buildSessionFactory();
            session = sessionFactory.getCurrentSession();
            log.info("Подключение к базе данных выполнено успешно!");
        } catch (Exception e) {
            log.error("Ошибка подключения к базе данных");
        }
        return session;
    }
}
