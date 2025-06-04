package org.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDB {
    private static Game game;

    public GameDB(Game game) {
        this.game = game;
    }

    public GameDB() {

    }
    public static void createTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS Games (" +
                "gameId INTEGER PRIMARY KEY, " +
                "gameName TEXT NOT NULL);";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            conn.commit();
            System.out.println("Table 'Games' created successfully or already exist.");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }
    public static void insert(Connection conn) {
        createTable(conn);
        String sql = "INSERT INTO Games (gameId,gameName) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, game.getgameId());
            pstmt.setString(2, game.getgameName());
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("game inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
        }
    }
    public static List<Game> select (Connection conn , List<String> columns , String condition){
        List<Game> games = new ArrayList<>();
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

        sql.append(" FROM Games");

        if (condition != null && !condition.isEmpty()) {
            sql.append(" WHERE ").append(condition);
        }
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql.toString());
            while (rs.next()) {
                Game game = new Game(
                        rs.getInt("id"),
                        rs.getString("name"));
                games.add(game);
            }
        } catch (SQLException e) {
            System.err.println("Error executing SELECT query: " + e.getMessage());
        }

        return games;


    }

}
