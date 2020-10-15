package edu.rseymour.advancedjava.model;

import jdk.nashorn.internal.ir.annotations.Immutable;
import javax.validation.constraints.NotNull;

import java.text.ParseException;
import java.util.Calendar;

/**
 * This class is used to a single query to stock service.
 */
@Immutable
public class StockQuery extends StockData{

    private final String symbol;
    private final Calendar from;
    private final Calendar until;

    /**
     * Create a new instance from string data. This constructor will convert
     * dates described as a String to Date objects.
     *
     * @param symbol the stock symbol
     * @param from   the start date as a string in the form of yyyy/MM/dd
     * @throws ParseException if the format of the date String is incorrect. If this happens
     *                        the only recourse is to try again with a correctly formatted String.
     */
    public StockQuery(@NotNull String symbol, @NotNull String from, @NotNull String until) throws ParseException {
        super();
        this.symbol = symbol;
        this.from = Calendar.getInstance();
        this.until = Calendar.getInstance();
        this.from.setTime(simpleDateFormat.parse(from));
        this.until.setTime(simpleDateFormat.parse(until));
    }

    /**
     * @return get the stock symbol associated with this query
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @return get the start Calendar associated with this query
     */
    public Calendar getFrom() {
        return from;
    }

    /**
     * @return get the end Calendar associated with this query
     */
    public Calendar getUntil() {
        return until;
    }
}
