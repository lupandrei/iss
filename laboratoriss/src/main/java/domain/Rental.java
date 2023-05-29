package domain;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name="rental")
public class Rental {

    public Rental() {

    }

    public void setIdRental(int idRental) {
        this.idRental = idRental;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "idRental=" + idRental +
                ", idClient='" + idClient + '\'' +
                ", idBook=" + idBook +
                ", rentedTime='" + rentedTime + '\'' +
                ", returnedTime='" + returnedTime + '\'' +
                ", copies=" + copies +
                ", status='" + status + '\'' +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRental;
    private String idClient;
    private int idBook;

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Rental(int idRental, String idClient, int idBook, String rentedTime, String returnedTime, int copies, String status) {
        this.idRental = idRental;
        this.idClient = idClient;
        this.idBook = idBook;
        this.rentedTime = rentedTime;
        this.returnedTime = returnedTime;
        this.copies = copies;
        this.status = status;
    }

    private String rentedTime;
    private String returnedTime;
    private int copies;

    private String status;


    public String getRentedTime() {
        return rentedTime;
    }

    public void setRentedTime(String rentedTime) {
        this.rentedTime = rentedTime;
    }

    public String getReturnedTime() {
        return returnedTime;
    }

    public void setReturnedTime(String returnedTime) {
        this.returnedTime = returnedTime;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }


    public int getIdRental() {
        return idRental;
    }
}
