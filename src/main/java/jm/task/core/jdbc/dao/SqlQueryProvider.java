package jm.task.core.jdbc.dao;

import java.io.IOException;
import java.util.Properties;

public class SqlQueryProvider {
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(SqlQueryProvider.class.getClassLoader().getResourceAsStream("sql.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить файл sql.properties", e);
        }
    }

    public static String getQuery(String key) {
        return properties.getProperty(key);
    }
}
