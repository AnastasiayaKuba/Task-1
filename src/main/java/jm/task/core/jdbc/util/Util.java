package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

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

    private static SessionFactory sessionFactory;

    public static Connection getConnection() {
        Connection connection = null;
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поключения к БД");
        }
        return connection;
    }

    public static SessionFactory getSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(User.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            System.out.println("Hibernate Connection successful!");
        } catch (Exception e) {
            System.err.println("Hibernate Connection failed: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
        return sessionFactory;
    }

//    public static Session getSession() {
//        Session session = null;
//        try {
//            Configuration configuration = new Configuration().addAnnotatedClass(User.class);
//            SessionFactory sessionFactory = configuration.buildSessionFactory();
//            session = sessionFactory.getCurrentSession();
//            log.info("Подключение к базе данных выполнено успешно!");
//        } catch (Exception e) {
//            log.error("Ошибка подключения к базе данных");
//        }
//        return session;
//    }
}