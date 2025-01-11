package jm.task.core.jdbc.dao;

public class SQLQueries {
    public static final String SQLCreate =
            "CREATE TABLE IF NOT EXISTS users("+
                    "id BIGSERIAL PRIMARY KEY,"+
                    "name VARCHAR(50) NOT NULL,"+
                    "lastname VARCHAR(50) NOT NULL,"+
                    "age SMALLINT NOT NULL)";
    public static final String SQLDrop =
            "DROP TABLE IF EXISTS users";
    public static final String SQLSave =
            "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";
    public static final String SQlRemove =
            "DELETE FROM users WHERE id =?";
    public static final String SQLGet =
            "SELECT * FROM users";
    public static final String SQLClean =
            "TRUNCATE TABLE users";
    public static final String SQLCheck =
            "SELECT COUNT(*) FROM users WHERE id= ?";
}