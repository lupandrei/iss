package domain;

import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name="librarian")
public class Librarian implements Entity<String>{

    @Id
    private String username;
    private String password;

    public Librarian() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Librarian(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void setID(String s) {
        username=s;
    }

    @Override
    public String getID() {
        return username;
    }
}
