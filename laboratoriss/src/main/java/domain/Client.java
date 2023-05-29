package domain;
import javax.persistence.*;

@javax.persistence.Entity
@Table(name="client")
public class Client implements Entity<String>{
    @Id
    private String username;
    private String cnp;
    private String fullName;
    private String email;
    private String password;
    private int penalty;

    public Client() {

    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public Client(String id,String password){
        this.username=id;
        this.password=password;
    }
    public Client(String id, String cnp, String fullName, String email, String password, int penalty) {
        this.username = id;
        this.cnp = cnp;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.penalty = penalty;
    }

    @Override
    public void setID(String id) {
        username=id;

    }

    @Override
    public String getID() {
        return username;
    }
}
