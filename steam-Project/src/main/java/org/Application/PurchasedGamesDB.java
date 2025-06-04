package org.Application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PurchasedGamesDB {

    public static void createTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS Purchases (" +
                "purchaseId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id INTEGER NOT NULL, " +
                "gameId INTEGER NOT NULL, " +
                "purchase_Date TEXT NOT NULL, " +
                "FOREIGN KEY (id) REFERENCES steamUsers(id), " +
                "FOREIGN KEY (gameId) REFERENCES Games(gameId)" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            conn.commit();
            System.out.println("Table 'Purchases' created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating Purchases table: " + e.getMessage());
        }
    }

    public static void insert(Connection conn, User user, Game game) {
        createTable(conn); // assure que la table existe

        String sql = "INSERT INTO Purchases (id, gameId, purchase_Date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getId());
            pstmt.setInt(2, game.getgameId());

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            pstmt.setString(3, formattedDateTime);

            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Game purchased successfully.");
        } catch (SQLException e) {
            System.err.println("Error purchasing game: " + e.getMessage());
        }
    }

    public static boolean hasUserPurchasedGame(Connection conn, User user, Game game) {
        String sql = "SELECT COUNT(*) FROM Purchases WHERE id = ? AND gameId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getId());
            pstmt.setInt(2, game.getgameId());
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking purchase: " + e.getMessage());
        }
        return false;
    }
}
