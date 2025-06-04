package org.Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class SignInController {
    public void SignInContrller(){

    }

    @FXML
    private Button registerButton;
    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password ;
    @FXML
    private PasswordField confirmedPassword;
    @FXML
    private Label wrongInfo;

    public void userRegister(ActionEvent event) throws IOException{
        checkRegister();
    }
    public void checkRegister() throws IOException{
        if(password.getText().isEmpty() || username.getText().isEmpty() || confirmedPassword.getText().isEmpty() || email.getText().isEmpty()) {
            wrongInfo.setText("Please Enter All The Informations");
        } else if (!password.getText().toString().equals(confirmedPassword.getText().toString())) {
            wrongInfo.setText("Please Check The Password");
        } else {

            Connection conn = DataBaseConnection.getConnection();
            UserDB.createTable(conn);

            List<User> usedEmail= UserDB.select (conn , null, "email = '"+email.getText()+"'");
            List<User> usedname= UserDB.select (conn , null, "name = '"+ username.getText()+"'");

            if(!usedEmail.isEmpty() || !usedname.isEmpty()) {
                wrongInfo.setText("This email or username is used");
            }


            else {
            User user = new User(0,username.getText(), email.getText(), password.getText());
            user.addUser();
            Main.changeScene("/login.fxml");}
        }



    }
    public void backToLogin() throws IOException {
        Main.changeScene("/login.fxml");
    }
}
