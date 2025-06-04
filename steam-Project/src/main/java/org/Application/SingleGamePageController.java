package org.Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class SingleGamePageController {

    @FXML
    private Button backButton;

    private User currentUser;
    private Game currentGame;
    private Connection conn;

    public void setUser(User user) {
        this.currentUser = user;
    }
    public void setGame(Game game) {
        this.currentGame = game;
    }

    public void backToGamesPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/games.fxml"));
        Parent root = loader.load();

        gamesPageController controller = loader.getController();
        controller.setCurrentUser(currentUser);

        Stage stage = Main.getPrimaryStage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void purchaseGame() throws IOException {
        if (currentUser == null || currentGame == null) {
            System.out.println("User or Game is not set!");
            return;
        }
        if (conn == null) {
            conn = DataBaseConnection.getConnection();
        }

        if (PurchasedGamesDB.hasUserPurchasedGame(conn, currentUser, currentGame)) {
            System.out.println("You have already purchased this game!");
            return;
        }

        PurchasedGamesDB.insert(conn, currentUser, currentGame);
        System.out.println("Purchase done, redirecting...");

        // Charger la page games.fxml en passant currentUser
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/games.fxml"));
        Parent root = loader.load();

        gamesPageController controller = loader.getController();
        controller.setCurrentUser(currentUser);  // IMPORTANT

        Stage stage = Main.getPrimaryStage();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
