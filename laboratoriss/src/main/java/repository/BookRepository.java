package repository;

import domain.Book;
import domain.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository implements IBookRepository{
    private String url;
    public BookRepository(String url) {
        this.url=url;
    }

    @Override
    public void add(Book entity) {
        Book book = findBookByName(entity.getName());
        if(book!=null){
            updateNoCopies(book.getID(),book.getCopies()+entity.getCopies());
        }
        else{
            String sql = "INSERT INTO book(name,author,copies) VALUES (?,?,?)";
            try(Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1,entity.getName());
                ps.setString(2,entity.getAuthor());
                ps.setInt(3, entity.getCopies());
                ps.executeUpdate();
            }
            catch(SQLException se){
                se.getMessage();
            }
        }

    }
    @Override
    public List<Book> getAll() {
        List<Book> books= new ArrayList<>();
        try(Connection connection=DriverManager.getConnection(url);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM book");
            ResultSet resultSet = ps.executeQuery()){
            while(resultSet.next()){
                int id =resultSet.getInt("id");
                String name = resultSet.getString("name");
                String author =resultSet.getString("author");
                int copies = resultSet.getInt("copies");
                Book book = new Book(id,name,author,copies);
                books.add(book);
            }
        }
        catch(SQLException se){
            se.printStackTrace();;
        }
        return books;
    }

    @Override
    public List<Book> getAllFilteredByAuthor(String author) {
        List<Book> books=new ArrayList<>();
        String sql ="SELECT * FROM book WHERE author=?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,author);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()) {
                String name = resultSet.getString("name");
                int id = resultSet.getInt("id");
                int copies = resultSet.getInt("copies");
                Book book = new Book(id,name,author,copies);
                books.add(book);

            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return books;
    }

    @Override
    public List<String> getAllAuthors() {
        List<String> authors=new ArrayList<>();
        try(Connection connection=DriverManager.getConnection(url);
            PreparedStatement ps = connection.prepareStatement("SELECT distinct author FROM book");
            ResultSet resultSet = ps.executeQuery()){
            while(resultSet.next()){

                String author =resultSet.getString("author");
                authors.add(author);
            }
        }
        catch(SQLException se){
            se.printStackTrace();;
        }
        return authors;
    }

    @Override
    public Book findBookByName(String name) {
        String sql ="SELECT * FROM book WHERE name=?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,name);
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next())
                return null;
            int id = resultSet.getInt("id");
            int copies = resultSet.getInt("copies");
            String author = resultSet.getString("author");
            Book book = new Book(id,name,author,copies);
            return book;
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateNoCopies(int id, int copies) {
        String sql = "UPDATE book SET copies=? WHERE id=?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setInt(1, copies);
            ps.setInt(2,id);
            ps.executeUpdate();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }

    @Override
    public Book findBookById(int id) {
        String sql ="SELECT * FROM book WHERE id=?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next())
                return null;
            int copies = resultSet.getInt("copies");
            String author = resultSet.getString("author");
            String name = resultSet.getString("name");
            Book book = new Book(id,name,author,copies);
            return book;
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return null;
    }
}
