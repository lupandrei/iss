package repository;

import domain.Client;

import java.sql.*;

public class ClientRepository implements IClientRepository{

    private String url;

    public ClientRepository(String url) {
        this.url=url;
    }

    @Override
    public Client findById(String s) {
        String sql ="SELECT * FROM client WHERE username=?";
        try(Connection connection = DriverManager.getConnection(url);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,s);
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next())
                return null;
            String username = resultSet.getString("username");
            String password =resultSet.getString("password");
            String email = resultSet.getString("email");
            String cnp =resultSet.getString("cnp");
            int penalty = resultSet.getInt("penalty");
            String name = resultSet.getString("fullname");

            Client client = new Client(username,cnp,name,email,password,penalty);
            return client;
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return null;
    }

    @Override
    public void updatePenaltyValue(int penaltyValue, String idClient) {

    }
}
