package org.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDB {
    private User user;

    public UserDB(User user) {
        this.user = user;
    }

    public UserDB() {

    }

    public static void createTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS steamUsers (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "password TEXT NOT NULL); ";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            conn.commit();
            System.out.println("Table 'steamUsers' created successfully or already exist.");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }
    public void insert(Connection conn) {
        createTable(conn);
        String sql = "INSERT INTO steamUsers (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("User inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
        }
    }
    public void delete(Connection conn, int userId) {
        String sql = "DELETE FROM steamUsers WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            if (rowsAffected > 0) {
                System.out.println("User with ID " + userId + " deleted successfully.");
            } else {
                System.out.println("No user found with ID " + userId);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }
    public static List<User> select (Connection conn , List<String> columns , String condition){
        List<User> users = new ArrayList<>();
        StringBuilder sql =new StringBuilder("SELECT ");
        if(columns == null || columns.isEmpty()){
            sql.append("*");
        } else {
            for (int i = 0; i < columns.size(); i++) {
                sql.append(columns.get(i));
                if (i < columns.size() - 1) {
                    sql.append(", ");
                }
            }
        }

        sql.append(" FROM steamUsers");

        if (condition != null && !condition.isEmpty()) {
            sql.append(" WHERE ").append(condition);
        }
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql.toString());
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error executing SELECT query: " + e.getMessage());
        }

        return users;


    }
    public void update(Connection conn) {
        System.out.println("Attempting to update user with ID: " + user.getId()); // Add this line

        String sql = "UPDATE steamUsers SET name = ?, email = ?, password = ?, WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, user.getId());
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            if (rowsAffected > 0) {
                System.out.println("User with ID " + user.getId() + " updated successfully.");
            } else {
                System.out.println("No user found with ID " + user.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }
}
