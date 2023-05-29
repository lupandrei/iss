package repository;

import domain.Book;

import java.util.List;

public interface IBookRepository extends Repository<Book, Integer> {
    List<Book> getAllFilteredByAuthor(String author);

    List<String> getAllAuthors();

    Book findBookByName(String name);

    void updateNoCopies(int id,int copies);

    Book findBookById(int id);
}
