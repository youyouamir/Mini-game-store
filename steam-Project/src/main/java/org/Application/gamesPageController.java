package org.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class gamesPageController {

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    private void switchToGame(String fxmlPath, int gameId, String gameName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        SingleGamePageController controller = loader.getController();

        Game game = new Game(gameId, gameName);
        controller.setUser(currentUser);
        controller.setGame(game);

        Stage stage = Main.getPrimaryStage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void switchToSpiderMan() throws IOException {
        switchToGame("/SpiderMan.fxml", 11, "SpiderMan");
    }

    public void switchToItTakesTwo() throws IOException {
        switchToGame("/ItTakesTwo.fxml", 9, "ItTakes2");
    }

    public void switchToMinecraft() throws IOException {
        switchToGame("/Minecraft.fxml", 10, "Minecraft");
    }

    public void switchToFortnite() throws IOException {
        switchToGame("/Fortnite.fxml", 7, "Fortnite");
    }

    public void switchToAstroBot() throws IOException {
        switchToGame("/AstroBot.fxml", 1, "AstroBot");
    }

    public void switchToEldenRing() throws IOException {
        switchToGame("/EledenRing.fxml", 6, "EledenRing");
    }

    public void switchToForza() throws IOException {
        switchToGame("/Forza.fxml", 8, "Forza");
    }

    public void switchToDreams() throws IOException {
        switchToGame("/Dreams.fxml", 5, "Dreams");
    }

    public void switchToZelda() throws IOException {
        switchToGame("/Zelda.fxml", 12, "Zelda");
    }

    public void switchToCyber() throws IOException {
        switchToGame("/Cyber.fxml", 3, "Cyber");
    }

    public void switchToCeleste() throws IOException {
        switchToGame("/Celeste.fxml", 2, "Celeste");
    }

    public void switchToDark() throws IOException {
        switchToGame("/Dark.fxml", 4, "dark");
    }

    public void switchToProfile() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profile.fxml"));
        Parent root = loader.load();

        ProfileController controller = loader.getController();
        controller.setUser(currentUser);
        controller.setConnection(DataBaseConnection.getConnection()); // ou une connexion déjà ouverte

        Stage stage = Main.getPrimaryStage();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
