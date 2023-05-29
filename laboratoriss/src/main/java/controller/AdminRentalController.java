package controller;

import domain.Book;
import domain.Client;
import domain.Librarian;
import domain.Rental;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import observers.observer;
import service.Service;

import java.net.PortUnreachableException;
import java.util.List;

public class AdminRentalController implements observer {

    public TableView tableView;
    public TableColumn<Rental,Integer> tableColumnIdClient;
    public TableColumn<Rental,Integer> tableColumnIdBook;
    public TableColumn<Rental,String> tableColumnReturned;
    public TableColumn<Rental,String> tableColumnStatus;
    public TableColumn<Rental,Integer> tableColumnCopies;
    public TableColumn<Rental,Integer> tableColumnIdRent;
    public ComboBox combobox;
    public Button buttonConfirmReturn;
    public TextField textFieldPenalty;
    private ObservableList<Rental> model = FXCollections.observableArrayList();
    private Service srv;
    private Librarian librarian;

    public void setData(Service srv,Librarian librarian){
        this.srv=srv;
        this.librarian=librarian;
        srv.add(this);
        combobox.getItems().add("all");
        combobox.getItems().add("rented");
        combobox.getItems().add("returned");
        combobox.getItems().add("waiting");

        initModel(srv.getAllRentals());
    }

    private void initModel(List<Rental> rentals) {

         model.setAll(rentals);
    }

    public void initialize(){
        tableColumnIdBook.setCellValueFactory(new PropertyValueFactory<Rental,Integer>("idBook"));
        tableColumnIdRent.setCellValueFactory(new PropertyValueFactory<Rental,Integer>("idRental"));
        tableColumnIdClient.setCellValueFactory(new PropertyValueFactory<Rental,Integer>("idClient"));
        tableColumnCopies.setCellValueFactory(new PropertyValueFactory<Rental,Integer>("copies"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<Rental,String>("status"));
        tableColumnReturned.setCellValueFactory(new PropertyValueFactory<Rental,String>("returnedTime"));
        tableView.setItems(model);
    }

    @Override
    public void update() {
        initModel(srv.getAllRentalsFilteredByStatus(combobox.getSelectionModel().getSelectedItem().toString()));
    }

    public void handleCombobox(ActionEvent actionEvent) {
        initModel(srv.getAllRentalsFilteredByStatus(combobox.getSelectionModel().getSelectedItem().toString()));

    }

    public void handleButtonConfirmReturn(ActionEvent actionEvent) {
        String penalty = textFieldPenalty.getText().toString();
        int penaltyValue;
        if(penalty.isEmpty()){
            penaltyValue = 0;
            Rental rental = (Rental) tableView.getSelectionModel().getSelectedItem();
            if(rental!=null){
                System.out.println("1");
                int idRental = rental.getIdRental();
                if(rental.getStatus().equals("waiting")){
                    Client client = srv.findClient(rental.getIdClient());
                    int newClientPenalty = client.getPenalty()+penaltyValue;
                    System.out.println("2");
                    srv.confirmReturn(idRental,newClientPenalty,rental.getIdClient());
                    System.out.println("3");
                    Book book = srv.findBook(rental.getIdBook());
                    int newBookStock = book.getCopies()+rental.getCopies();
                    System.out.println("4");
                    srv.updateStock(rental.getIdRental(), newBookStock);
                    System.out.println("5");
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText("Incorrect status");
                    alert.showAndWait();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Select an item");
                alert.showAndWait();
            }
        }
        else{
            penaltyValue = Integer.valueOf(penalty);
            System.out.println(penaltyValue);
            if(penaltyValue>0){
                Rental rental = (Rental) tableView.getSelectionModel().getSelectedItem();
                if(rental!=null){
                    System.out.println("1");
                    int idRental = rental.getIdRental();
                    if(rental.getStatus().equals("waiting")){
                        int newClientPenalty = srv.findClient(rental.getIdClient()).getPenalty()+penaltyValue;
                        System.out.println("2");
                        srv.confirmReturn(idRental,newClientPenalty,rental.getIdClient());
                        System.out.println("3");
                        int newBookStock = srv.findBook(rental.getIdBook()).getCopies()+rental.getCopies();
                        System.out.println("4");
                        srv.updateStock(rental.getIdRental(), newBookStock);
                        System.out.println("5");
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error");
                        alert.setContentText("Incorrect status");
                        alert.showAndWait();
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText("Select an item");
                    alert.showAndWait();
                }

            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Invalid penalty");
                alert.showAndWait();
            }
        }

    }
}
