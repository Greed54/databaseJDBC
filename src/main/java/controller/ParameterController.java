package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.Parameter;
import utils.DialogManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ParameterController {
    private Stage parameterStage;

    private Main main;

    private int idParameter;

    private int lastIdParameter;

    @FXML
    private ComboBox<Integer> par_choiceBox;
    @FXML
    private DatePicker date_picker;
    @FXML
    private TextField cmd_weightTare;
    @FXML
    private TextField cmd_cost;

    public void init(){
        setParameterWindow();
        initListeners();
    }

    public void setParameterStage(Stage parameterStage){
        this.parameterStage = parameterStage;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    private void setParameterWindow() {
        main.getCollections().clearParameters();
        main.getCollections().clearOptions();
        main.getDatabase().initializeParameters(main);
        par_choiceBox.setItems(main.getCollections().getOptions());
    }

    private void initListeners() {
        par_choiceBox.getSelectionModel().selectedItemProperty().addListener ((observable, oldValue, newValue) -> {

            if (newValue != null) {
                lastIdParameter = newValue;
                date_picker.setValue(parseDate(main.getDatabase().getParameterOnDatabase(newValue).getData()));
                cmd_weightTare.setText(main.getDatabase().getParameterOnDatabase(newValue).getTareWeight());
                cmd_cost.setText(main.getDatabase().getParameterOnDatabase(newValue).getCost1Kg());
            }
        });
    }


    @FXML
    private void changeParameter() {

        int count = 0;

        for (Parameter p : main.getCollections().getParameters()) {
            if (date_picker.getValue().equals(parseDate(p.getData()))) {
                count++;
            }
        }

        if (count == 0){
            if (DialogManager.showConfirmationDialog("Нет параметра", "Параметра с такой датой не существует! Вы хотите добавить новый параметр? Если хотите просто изменить дату, нажмите НЕТ") == 1) {
                main.getDatabase().addParameterInDatabase(date_picker.getValue(), cmd_weightTare.getText(), cmd_cost.getText());
                setParameterWindow();
            }
        } else {
            main.getDatabase().changeParameterAndUpdateDate(date_picker.getValue(), cmd_weightTare.getText(), cmd_cost.getText(), par_choiceBox);
            setParameterWindow();
        }

    }

    @FXML
    private void saveParameter() {
        idParameter = par_choiceBox.getSelectionModel().getSelectedItem();
        main.getDatabase().updateParameterInTableBox(idParameter);
        parameterStage.close();
    }

    @FXML
    private void saveParameterAndDeleteBox () {
        idParameter = par_choiceBox.getSelectionModel().getSelectedItem();
        main.getDatabase().updateParameterWithZeroKilo(idParameter);
        parameterStage.close();
    }

    private LocalDate parseDate (String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

    public int getLastIdParameter() {
        return lastIdParameter;
    }

}
