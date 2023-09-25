package org.example.api.user;

import org.example.api.user.entity.User;
import org.example.common.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDatabaseService {
    private Connection connection;

    public  UserDatabaseService() throws SQLException {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        this.connection = dbConnection.getConnection();
    }

    public Boolean addUser(User user) throws SQLException {
        String query = String.format(
                "INSERT INTO users (email, name, password) VALUES ('%s', '%s', '%s')",
                user.getEmail(),
                user.getName(),
                user.getPassword()
        );
        this.connection.createStatement().execute(query);
        return true;
    }

    public List<User> getUsers() throws SQLException {
        ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM users");
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(
                new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                )
            );
        }
        return users;
    }

    public User getUserByEmail(String email) throws SQLException {
        String query = String.format("SELECT * FROM users WHERE email = '%s'", email);
        ResultSet rs = this.connection.createStatement().executeQuery(query);
        rs.next();
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password")
        );
    }
}
