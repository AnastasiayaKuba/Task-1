package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Валерия", "Бубнова", (byte) 23);
        userService.saveUser("Владислав", "Балякин", (byte) 22);
        userService.saveUser("Дмитрий", "Жаворонков", (byte) 23);
        userService.saveUser("Анна", "Китаева", (byte) 22);
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            System.out.println(user.toString());
        }
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
