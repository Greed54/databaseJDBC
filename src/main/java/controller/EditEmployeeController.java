package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.Employee;
import model.DatabaseConnector;
import utils.DialogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EditEmployeeController {
    private Main main;

    @FXML
    private TextField cmd_name_e;
    @FXML
    private TextField cmd_selo_e;
    @FXML
    private Stage editEmployeeStage;

    private Employee employee;

    public void setEditEmployeeStage(Stage editEmployeeStage){this.editEmployeeStage = editEmployeeStage;}

    public void setMain(Main main){
        this.main = main;
    }

    public void setEmployee(Employee employee){
        if (employee == null){
            return;
        }
        this.employee = employee;
        cmd_name_e.setText(employee.getName());
        cmd_selo_e.setText(employee.getSelo());
    }


    @FXML
    private void actionSave() {
        if (!checkValues()){
            return;
        }
        main.getDatabase().updateEmployeeInDatabase(employee, cmd_selo_e.getText(), cmd_name_e.getText(), main);
        editEmployeeStage.close();
    }

    private boolean checkValues(){
        if (cmd_name_e.getText().trim().length() == 0 || cmd_selo_e.getText().trim().length() == 0){
            DialogManager.showErrorDialog("Ошибка!", "Одно или несколько полей пустое! Заполните поля!");
            return false;
        }
        return true;
    }
}
