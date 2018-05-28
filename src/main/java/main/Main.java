package main;

import controller.LoginWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import controller.DatabaseWindowController;
import controller.EditEmployeeController;
import controller.ParameterController;
import model.CollectionsWithData;
import model.DatabaseHandler;
import model.Employee;
import model.DatabaseConnector;


public class Main extends Application {

    private Stage primaryStage;

    private DatabaseHandler handler = new DatabaseHandler();

    private CollectionsWithData collections = new CollectionsWithData();

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Login");
        DatabaseConnector.connectionDataBase();
        showLoginWindow();
    }

    private void showLoginWindow(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/loginWindow.fxml"));
            Pane pane = loader.load();
            primaryStage.setScene(new Scene(pane, 600, 600));

            LoginWindowController loginWindowController = loader.getController();
            loginWindowController.setMain(this);
            LoginWindowController.stage = primaryStage;

            primaryStage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private DatabaseWindowController controller;

    public void showDatabaseWindow(Main main){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/databaseWindow.fxml"));
            Pane pane = loader.load();
            primaryStage.setTitle("Database");
            primaryStage.setScene(new Scene(pane));

            controller = loader.getController();
            controller.setMain(main);
            controller.init();

            primaryStage.show();
            showParameterWindow(main);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showParameterWindow(Main main){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/parameterWindow.fxml"));
            Pane pane = loader.load();

            Stage parameterStage = new Stage();
            parameterStage.setTitle("ParameterWindow");
            parameterStage.initModality(Modality.WINDOW_MODAL);
            parameterStage.initOwner(primaryStage);
            parameterStage.setScene(new Scene(pane));

            ParameterController controllerP = loader.getController();
            controllerP.setMain(main);
            controllerP.setParameterStage(parameterStage);
            controllerP.init();

            parameterStage.showAndWait();
            controller.setIdParameter(controllerP.getLastIdParameter());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showEditEmployeeWindow(Employee employee, Main main){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/editEmployeeWindow.fxml"));
            Pane pane = loader.load();

            Stage editEmployeeStage = new Stage();
            editEmployeeStage.setTitle("EditEmployeeWindow");
            editEmployeeStage.initModality(Modality.WINDOW_MODAL);
            editEmployeeStage.initOwner(primaryStage);
            editEmployeeStage.setScene(new Scene(pane));

            EditEmployeeController controllerEE = loader.getController();
            controllerEE.setEditEmployeeStage(editEmployeeStage);
            controllerEE.setMain(main);
            controllerEE.setEmployee(employee);

            editEmployeeStage.showAndWait();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public DatabaseHandler getDatabase() {
        return handler;
    }

    public CollectionsWithData getCollections() {
        return collections;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

