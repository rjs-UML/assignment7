package edu.rseymour.advancedjava.model.database;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "quote", schema = "stocks")
public class QuoteDAO implements DatabasesAccessObject {
    private int id;
    private Timestamp time;
    private BigDecimal price;
    private StockSymbolDAO stockSymbolBySymbolId;

    @Id
    @Column(name = "id",  nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "time", nullable = false, insertable = true, updatable = true)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Digits(integer=5, fraction=2)
    @Column(name = "price", nullable = false, insertable = true, updatable = true, precision = 0)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuoteDAO quoteDAO = (QuoteDAO) o;

        if (id != quoteDAO.id) return false;
        if (price != quoteDAO.price) return false;
        if (time != null ? !time.equals(quoteDAO.time) : quoteDAO.time != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + price.hashCode();
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "symbol_id", referencedColumnName = "id", nullable = false)
    public StockSymbolDAO getStockSymbolBySymbolId() {
        return stockSymbolBySymbolId;
    }

    public void setStockSymbolBySymbolId(StockSymbolDAO stockSymbolBySymbolId) {
        this.stockSymbolBySymbolId = stockSymbolBySymbolId;
    }
}
