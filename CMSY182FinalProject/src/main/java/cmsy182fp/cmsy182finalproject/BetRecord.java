/**
 * BetRecord- records bet entry as object
 * CMSY167 Spring 2023
 * @author Samuel Dolle
 * @version 1.0
 *
 */
package cmsy182fp.cmsy182finalproject;

import java.time.LocalDate;

public class BetRecord {
    private LocalDate date;
    private String name;
    private Double amountWagered;
    private Double odds;
    private Double amountWon;

   public BetRecord(LocalDate Date, String Name, Double AmountWagered, Double Odds, Double AmountWon) {
        this.date = Date;
        this.name = Name;
        this.amountWagered = AmountWagered;
        this.odds = Odds;
        this.amountWon = AmountWon; //constructor created with bet entry fields as parameters
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmountWagered() {
        return amountWagered;
    }

    public void setAmountWagered(Double amountWagered) {
        this.amountWagered = amountWagered;
    }

    public Double getOdds() {
        return odds;
    }

    public void setOdds(Double odds) {
        this.odds = odds;
    }

    public Double getAmountWon() {
        return amountWon;
    }

    public void setAmountWon(Double amountWon) {
        this.amountWon = amountWon;
    }

    @Override
    public String toString() {
        return String.format("Date: %s\r\nName: %s\r\nAmountWagered: %.2f\r\nOdds: %.2f\r\nAmountWon: %.2f\r\n",
                date, name, amountWagered, odds, amountWon);
    }
}
