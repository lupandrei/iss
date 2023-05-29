package repository;

import domain.Rental;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalRepository implements IRentalRepository{

    private String url;
    public RentalRepository(String url) {
        this.url=url;
    }

    @Override
    public void add(Rental entity) {
        String sql = "INSERT INTO rental(idclient,idbook,rentedtime,returntime,copies,status) VALUES (?,?,?,?,?,?)";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,entity.getIdClient());
            ps.setInt(2,entity.getIdBook());
            ps.setString(3, String.valueOf(entity.getRentedTime()));
            ps.setString(4, String.valueOf(entity.getReturnedTime()));
            ps.setInt(5,entity.getCopies());
            ps.setString(6,entity.getStatus());
            ps.executeUpdate();
        }
        catch(SQLException se){
            se.getMessage();
        }
    }

    @Override
    public List<Rental> getAll() {
        List<Rental> rentals= new ArrayList<>();
        try(Connection connection=DriverManager.getConnection(url);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM rental");
            ResultSet resultSet = ps.executeQuery()){
            while(resultSet.next()){
                int idRental = resultSet.getInt("id");
                String idClient =resultSet.getString("idclient");
                int idBook = resultSet.getInt("idbook");
                String rentedtime =resultSet.getString("rentedtime");
                String returnedtime =resultSet.getString("returntime");
                int copies = resultSet.getInt("copies");
                String status = resultSet.getString("status");


                Rental rent = new Rental(idRental, idClient,idBook,rentedtime,returnedtime,copies, status);
                rentals.add(rent);
            }
        }
        catch(SQLException se){
            se.printStackTrace();;
        }
        return rentals;
    }

    @Override
    public void updateRentalStatus(int idRental, String newStatus) {
        String sql = "Update rental set status=? where id=?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,newStatus);
            ps.setInt(2,idRental);
            ps.executeUpdate();
        }
        catch(SQLException se){
            se.getMessage();
        }
    }
}
