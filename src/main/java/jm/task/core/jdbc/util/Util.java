package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(Util.class.getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить файл database.properties", e);
        }
    }

    public static Connection getConnection() {
        Connection conn;
        try {
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Подключение к базе данных выполнено успешно!");
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к базе данных");
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
        return conn;
    }

    public static Session getSession() {
        Session session;
        try {
            Configuration configuration = new Configuration();

            configuration.setProperty("hibernate.connection.url", properties.getProperty("db.url"));
            configuration.setProperty("hibernate.connection.username", properties.getProperty("db.username"));
            configuration.setProperty("hibernate.connection.password", properties.getProperty("db.password"));
            configuration.setProperty("hibernate.dialect", properties.getProperty("hibernate.dialect"));
            configuration.setProperty("hibernate.show_sql", properties.getProperty("hibernate.show_sql"));
            configuration.setProperty("hibernate.hbm2ddl.auto", properties.getProperty("hibernate.hbm2ddl.auto"));

            configuration.addAnnotatedClass(User.class);

            SessionFactory sessionFactory = configuration.buildSessionFactory();
            session = sessionFactory.openSession();

            System.out.println("Подключение к базе данных через Hibernate выполнено успешно!");
        } catch (Exception e) {
            System.err.println("Ошибка подключения к базе данных через Hibernate");
            throw new RuntimeException("Ошибка подключения к базе данных через Hibernate", e);
        }
        return session;
    }
}
