package controller;

import com.example.laboratoriss.main;
import domain.Book;
import domain.Librarian;
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
import java.util.List;

public class AdminController implements observer {
    public TableView<Book> tableView;
    public TableColumn<Book,Integer> tableColumnId;
    public TableColumn<Book,String> tableColumnTitle;
    public TableColumn<Book,String> tableColumnAuthor;
    public TableColumn<Book,Integer> tableColumnCopies;
    public TextField textFieldTitle;
    public TextField textFieldAuthor;
    public TextField textFieldCopies;
    public Button addButton;
    public Button rentalButton;
    private ObservableList<Book> model = FXCollections.observableArrayList();

    private Service srv;
    private Librarian librarian;

    public void setData(Service srv,Librarian librarian){
        this.srv=srv;
        this.librarian=librarian;
        srv.add(this);
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

    public void handleAddButton(ActionEvent actionEvent) {
        String name = textFieldTitle.getText().toString();
        String author = textFieldAuthor.getText().toString();
        int copies = Integer.parseInt(textFieldCopies.getText().toString());
        if(name!=null  && author !=null && copies>0){
            Book book = new Book(0,name,author,copies);
            srv.addBook(book);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("The fields should not be empty!Modify number of copies to a natural number bigger than 0");
            alert.showAndWait();
        }
    }

    @Override
    public void update() {
        initModel(srv.getBooksFilteredByAuthor("all"))  ;
    }

    public void handleRentalButton(ActionEvent actionEvent) {
        Stage currentStage = (Stage) textFieldAuthor.getScene().getWindow();
        //currentStage.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("views/adminBookReturnsView.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 920, 460);
            stage.setTitle(librarian.getID());
            stage.setScene(scene);

            AdminRentalController adminRentalController =fxmlLoader.getController();
            adminRentalController.setData(srv,librarian);

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
