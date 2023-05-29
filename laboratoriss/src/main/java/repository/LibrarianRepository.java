package repository;

import domain.Client;
import domain.Librarian;

import java.sql.*;

public class LibrarianRepository implements ILibrarianRepository{
    private String url;

    public LibrarianRepository(String url) {
        this.url=url;
    }

    @Override
    public Librarian findById(String s) {
        String sql ="SELECT * FROM librarian WHERE username=?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,s);
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next())
                return null;
            String password =resultSet.getString("password");
            Librarian librarian= new Librarian(s,password);
            return librarian;
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return null;
    }

}
