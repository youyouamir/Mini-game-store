package org.Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class LogInController {
    public void LogInContrller(){

    }
    @FXML
    private Button loginButton;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password ;
    @FXML
    private Label wrongInfo;

    public void userLogin(ActionEvent event) throws IOException{
        checkLogin();
    }
    @FXML
    public void checkLogin() throws IOException {
        Connection conn = DataBaseConnection.getConnection();
        UserDB.createTable(conn);

        List<User> matchedUsers = UserDB.select(conn, null,
                "name = '"+username.getText()+"' AND password = '"+password.getText()+"'");

        if (!matchedUsers.isEmpty()) {
            User loggedInUser = matchedUsers.get(0);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/games.fxml"));
            Parent root = loader.load();

            gamesPageController controller = loader.getController();
            controller.setCurrentUser(loggedInUser);

            Stage stage = Main.getPrimaryStage();
            stage.setScene(new Scene(root));
            stage.show();
        } else if (username.getText().isEmpty() || password.getText().isEmpty()) {
            wrongInfo.setText("Please Enter All The Informations");
        } else {
            wrongInfo.setText("The Username Or Password Is Wrong");
        }
    }

    public void userSignUp(ActionEvent event) throws IOException{
        Main.changeScene("/signUp.fxml");


    }




}
