package service;

import domain.*;
import observers.observable;
import observers.observer;
import repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Service implements observable {
    private IBookRepository bookRepository;
    private IClientRepository clientRepository;
    private ILibrarianRepository librarianRepository;

    private IRentalRepository rentalRepository;
    private List<observer> obs;

    public Service(IBookRepository bookRepository, IClientRepository clientRepository, ILibrarianRepository librarianRepository, IRentalRepository rentalRepository) {
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
        this.librarianRepository = librarianRepository;
        this.rentalRepository = rentalRepository;
        obs=new ArrayList<>();
    }


    @Override
    public void notifyObservers() {
        for(observer o:obs){
            o.update();
        }
    }

    @Override
    public void add(observer o) {
        obs.add(o);
    }

    public void loginLibrarian(Librarian librarian) throws ServiceException {
        Librarian lib = librarianRepository.findById(librarian.getID());
        if(lib==null){
            throw new ServiceException("User does not exist!");
        }
        if(!lib.getPassword().equals(librarian.getPassword()))
            throw new ServiceException("Invalid password!");
    }

    public void loginClient(Client client) throws ServiceException {
        Client cl = clientRepository.findById(client.getID());
        if(cl==null){
            throw new ServiceException("User does not exist!");
        }
        if(!cl.getPassword().equals(client.getPassword())){
            throw new ServiceException("Invalid password");
        }
    }

    public List<Book> getBooksFilteredByAuthor(String author) {
        if(author.equals("all")){
            return bookRepository.getAll();
        }
        return bookRepository.getAllFilteredByAuthor(author);
    }

    public List<String> getAllAuthors() {
        return bookRepository.getAllAuthors();
    }

    public void addBook(Book book) {
        bookRepository.add(book);
        notifyObservers();
    }

    public void addRental(Rental rent) {
        rentalRepository.add(rent);
        bookRepository.updateNoCopies(rent.getIdBook(),bookRepository.findBookById(rent.getIdBook()).getCopies()-rent.getCopies());
        notifyObservers();
    }

    public List<RentalDTO> getAllRentals(String id) {
        List<Rental> rentals = rentalRepository.getAll();
        List<RentalDTO> rentalDTOS = new ArrayList<>();
        for(Rental rental: rentals){
            if(rental.getIdClient().equals(id) && rental.getStatus().equals("rented")){
                Book book = bookRepository.findBookById(rental.getIdBook());
                RentalDTO rentalDTO = new RentalDTO(rental.getIdRental(), book.getName(),book.getAuthor(),rental.getRentedTime(),rental.getReturnedTime(),rental.getCopies());
                rentalDTOS.add(rentalDTO);
            }
        }
        return rentalDTOS;
    }
    public List<Rental> getAllRentals(){
        return rentalRepository.getAll();
    }

    public List<Rental> getAllRentalsFilteredByStatus(String status) {
        List<Rental> allRentals = rentalRepository.getAll();
        if(status.equals("all")){
            return getAllRentals();
        }
        List<Rental> filteredRentals = allRentals.stream()
                .filter(rental -> rental.getStatus().equals(status))
                .collect(Collectors.toList());
        return filteredRentals;

    }

    public void returnRental(int idRental) {
        rentalRepository.updateRentalStatus(idRental,"waiting");
        notifyObservers();
    }

    public void confirmReturn(int idRental, int penaltyValue, String idClient) {
        rentalRepository.updateRentalStatus(idRental,"returned");
        clientRepository.updatePenaltyValue(penaltyValue,idClient);
    }
    public Client findClient(String idClient){
        return clientRepository.findById(idClient);
    }

    public Book findBook(int idBook) {
        return bookRepository.findBookById(idBook);
    }

    public void updateStock(int idRental, int newBookStock) {
        bookRepository.updateNoCopies(idRental,newBookStock);
        notifyObservers();
    }
}
