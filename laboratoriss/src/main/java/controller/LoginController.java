package controller;

import com.example.laboratoriss.main;
import domain.Client;
import domain.Librarian;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.Service;
import service.ServiceException;

import java.io.IOException;

public class LoginController {
    public TextField usernameField;
    public PasswordField passwordField;
    public Button buttonLogin;
    private Service srv;

    public void setData(Service srv){
        this.srv=srv;
    }

    public void handleLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(username!=null && password!=null){
            if(username.endsWith("librarian")){
                Librarian librarian = new Librarian(username,password);
                try{
                    srv.loginLibrarian(librarian);
                    loadLibrarian(librarian);
                }
                catch(ServiceException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
            else{
                Client client = new Client(username,password);
                try {
                    srv.loginClient(client);
                    loadClient(client);
                } catch (ServiceException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Invalid data provided!");
            alert.showAndWait();
        }
    }

    private void loadClient(Client client) {
        Stage currentStage = (Stage) passwordField.getScene().getWindow();
        //currentStage.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("views/clientView.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 920, 460);
            stage.setTitle(client.getID());
            stage.setScene(scene);

            ClientController clientController =fxmlLoader.getController();
            clientController.setData(srv,client);

            stage.show();
        }
        catch(IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Error!");
            alert.showAndWait();
        }
    }

    private void loadLibrarian(Librarian librarian) {
        Stage currentStage = (Stage) passwordField.getScene().getWindow();
        //currentStage.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("views/adminView.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 920, 460);
            stage.setTitle(librarian.getID());
            stage.setScene(scene);

            AdminController adminController =fxmlLoader.getController();
            adminController.setData(srv,librarian);

            stage.show();
        }
        catch(IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Error!");
            alert.showAndWait();
        }
    }
}
