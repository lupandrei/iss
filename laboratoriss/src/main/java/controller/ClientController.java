package controller;

import com.example.laboratoriss.main;
import domain.Book;
import domain.Client;
import domain.Rental;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import observers.observer;
import service.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ClientController implements observer {
    public TextField textFieldCopies;
    public ComboBox comboboxFilterByAuthor;
    public TableView<Book> tableView;
    public TableColumn<Book,Integer> tableColumnId;
    public TableColumn<Book,String> tableColumnTitle;
    public TableColumn<Book,String> tableColumnAuthor;
    public TableColumn<Book,Integer> tableColumnCopies;
    public Button rentedBooksButton;
    private ObservableList<Book> model = FXCollections.observableArrayList();
    private Service srv;
    private Client client;
    public void setData(Service srv, Client client) {
        this.srv=srv;
        this.client=client;
        srv.add(this);
        comboboxFilterByAuthor.getItems().add("all");
        for(String author:srv.getAllAuthors()){
            comboboxFilterByAuthor.getItems().add(author);
        }

        initModel(srv.getBooksFilteredByAuthor("all"));
    }

    private void initModel(List<Book> all) {
        model.setAll(all);
    }

    public void initialize(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Book,Integer>("ID"));
        tableColumnAuthor.setCellValueFactory(new PropertyValueFactory<Book,String>("author"));
        tableColumnTitle.setCellValueFactory(new PropertyValueFactory<Book,String>("name"));
        tableColumnCopies.setCellValueFactory(new PropertyValueFactory<Book,Integer>("copies"));
        tableView.setItems(model);
    }

    @Override
    public void update() {
        if(comboboxFilterByAuthor.getSelectionModel().getSelectedItem()!=null)
            initModel(srv.getBooksFilteredByAuthor(comboboxFilterByAuthor.getSelectionModel().getSelectedItem().toString()));
        else
            initModel(srv.getBooksFilteredByAuthor("all"));
    }

    public void handleComboBox(ActionEvent actionEvent) {
        String author =comboboxFilterByAuthor.getSelectionModel().getSelectedItem().toString();
        initModel(srv.getBooksFilteredByAuthor(author));
    }

    public void handleButtonRent(ActionEvent actionEvent) {
        if(textFieldCopies.getText()!=null && tableView.getSelectionModel().getSelectedItem()!=null){
            int copies = Integer.parseInt(textFieldCopies.getText().toString());
            int available = tableView.getSelectionModel().getSelectedItem().getCopies();
            if(copies>available || copies<=0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Number of copies should be smaller than " +available +" and bigger than 0");
                alert.showAndWait();
            }
            else{
                if(client.getPenalty()>50){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText("Penalty score is higher than 50!");
                    alert.showAndWait();
                }
                else{
                    Book selectedItem = tableView.getSelectionModel().getSelectedItem();
                    int idBook = tableColumnId.getCellObservableValue(selectedItem).getValue();
                    Rental rent = new Rental(1000, client.getID(),idBook,String.valueOf(LocalDate.now()), String.valueOf(LocalDate.now().plusDays(7)),copies, "rented");
                    srv.addRental(rent);
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("You did not select any item from the table or you did not complete the number of copies!");
            alert.showAndWait();
        }

    }

    public void handleRentedBooksButton(ActionEvent actionEvent) {
        Stage currentStage = (Stage) textFieldCopies.getScene().getWindow();
        //currentStage.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("views/clientReservedBooksView.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 920, 460);
            stage.setTitle(client.getID());
            stage.setScene(scene);

            RentalController rentalController =fxmlLoader.getController();
            rentalController.setData(srv,client);

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
