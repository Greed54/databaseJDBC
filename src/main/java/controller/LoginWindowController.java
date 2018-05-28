package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import utils.DialogManager;

public class LoginWindowController {
    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField txt_password;
    @FXML
    public static Stage stage;

    private Main main;

    public void setMain(Main main){this.main = main;}

    @FXML
    private void login() {
        if (main.getDatabase().checkLoginAndPass(txt_username.getText(), txt_password.getText())){
            main.showDatabaseWindow(main);
        } else {
            DialogManager.showErrorDialog("Подключение не удалось!","Такого пользователя не существует, или логин и пароль не верны.");
        }
    }
}
