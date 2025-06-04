package org.Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextArea ownedGamesField;

    private User currentUser;
    private Connection conn;

    public void setUser(User user) {
        this.currentUser = user;
        tryLoadData();
    }

    public void setConnection(Connection conn) {
        this.conn = conn;
        tryLoadData();
    }

    private void tryLoadData() {
        if (currentUser != null && conn != null) {
            loadUserData();
        }
    }

    private void loadUserData() {
        usernameField.setText(currentUser.getName());
        emailField.setText(currentUser.getEmail());
        loadOwnedGames();
    }

    private void loadOwnedGames() {
        StringBuilder gamesList = new StringBuilder();
        String sql = "SELECT g.gameName FROM Games g " +
                "JOIN Purchases p ON g.gameId = p.gameId " +
                "WHERE p.id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currentUser.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                gamesList.append("- ").append(rs.getString("gameName")).append("\n");
            }

            if (gamesList.length() == 0) {
                gamesList.append("You have no purchased games.");
            }

            ownedGamesField.setText(gamesList.toString());

        } catch (SQLException e) {
            ownedGamesField.setText("Error loading games: " + e.getMessage());
        }
    }

    public void switchToGames() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/games.fxml"));
        Parent root = loader.load();

        gamesPageController controller = loader.getController();
        controller.setCurrentUser(currentUser);

        Stage stage = Main.getPrimaryStage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void logout() throws IOException {
        Main.changeScene("/login.fxml");
    }
    @FXML
    private void deleteAccount() {
        if (currentUser == null || conn == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "User or database connection not set.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Account Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete your account?");
        confirmAlert.setContentText("This action cannot be undone.");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Purchases WHERE id = ?")) {
                        pstmt.setInt(1, currentUser.getId());
                        pstmt.executeUpdate();
                    }
                    try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM steamUsers WHERE id = ?")) {
                        pstmt.setInt(1, currentUser.getId());
                        int affected = pstmt.executeUpdate();
                        if (affected > 0) {
                            conn.commit();
                            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Your account has been deleted.");
                            try {
                                Main.changeScene("/login.fxml");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Account deletion failed.");
                        }
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
                }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
