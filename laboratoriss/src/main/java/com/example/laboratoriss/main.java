package com.example.laboratoriss;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.*;
import service.Service;

import java.io.IOException;

public class main extends Application {
    private Service srv;

    public static void main(String [] args){
        launch(args);
    }
    public void start(Stage primaryStage)throws IOException {
        String url = "jdbc:sqlite:iss";
        IClientRepository clientRepo = new ClientHBNRepository();
        IBookRepository bookRepo = new BookHBNRepository();
        ILibrarianRepository librarianRepo = new LibrarianRepository(url);
        IRentalRepository rentalRepository = new RentalRepository(url);
        srv= new Service(bookRepo,clientRepo,librarianRepo, rentalRepository);
        initView(primaryStage);
        primaryStage.show();

    }
    private void initView(Stage primaryStage) throws IOException{
        FXMLLoader loader =new FXMLLoader(getClass().getResource("views/loginView.fxml"));
        Scene scene = new Scene(loader.load(),400,400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Log in");

        LoginController controllerlogin = loader.getController();
        controllerlogin.setData(srv);
    }
}
