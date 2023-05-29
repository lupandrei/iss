package repository;

import domain.Book;
import domain.Rental;

import java.util.List;

public interface IRentalRepository extends Repository<Rental, Integer> {

    void updateRentalStatus(int idRental, String newStatus);
}
