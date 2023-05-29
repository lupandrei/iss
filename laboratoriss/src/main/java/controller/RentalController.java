package controller;

import domain.Book;
import domain.Client;
import domain.RentalDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import observers.observer;
import service.Service;

public class RentalController implements observer {

    public TableColumn<RentalDTO,Integer> tableColumnIdRental;
    private Service srv;
    private Client client;
    public Button buttonReturn;

    public TableView<RentalDTO> tableView;
    public TableColumn<RentalDTO,String > tableColumnTitle;
    public TableColumn<RentalDTO,String> tableColumnAuthor;
    public TableColumn<RentalDTO,String> tableColumnLoanDate;
    public TableColumn<RentalDTO,String> tableColumnReturnDate;
    public TableColumn<RentalDTO,Integer> tableColumnCopies;
    private ObservableList<RentalDTO> model = FXCollections.observableArrayList();

    public void setData(Service srv , Client client){
        this.srv=srv;
        this.client=client;
        srv.add(this);
        initModel();
    }

    private void initModel() {
        model.setAll(srv.getAllRentals(client.getID()));
    }

    public void initialize(){
        tableColumnAuthor.setCellValueFactory(new PropertyValueFactory<RentalDTO,String>("author"));
        tableColumnTitle.setCellValueFactory(new PropertyValueFactory<RentalDTO,String>("title"));
        tableColumnCopies.setCellValueFactory(new PropertyValueFactory<RentalDTO,Integer>("copies"));
        tableColumnLoanDate.setCellValueFactory(new PropertyValueFactory<RentalDTO,String>("loanDate"));
        tableColumnReturnDate.setCellValueFactory(new PropertyValueFactory<RentalDTO,String>("returnDate"));
        tableColumnIdRental.setCellValueFactory(new PropertyValueFactory<RentalDTO,Integer>("idRental"));
        tableView.setItems(model);
    }

    public void handleButtonReturn(ActionEvent actionEvent) {
        int idRental = tableView.getSelectionModel().getSelectedItem().getIdRental();
        srv.returnRental(idRental);

    }

    @Override
    public void update() {
        initModel();
    }
}
