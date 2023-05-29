package domain;

public class RentalDTO {
    private int idRental;
    private String title;
    private String author;
    private String loanDate;
    private String returnDate;
    private int copies;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public RentalDTO(int idRental, String title, String author, String loanDate, String returnDate, int copies) {
        this.idRental = idRental;
        this.title = title;
        this.author = author;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.copies = copies;
    }

    public int getIdRental() {
        return idRental;
    }

    public void setIdRental(int idRental) {
        this.idRental = idRental;
    }
}
