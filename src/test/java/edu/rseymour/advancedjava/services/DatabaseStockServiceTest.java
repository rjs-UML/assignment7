package edu.rseymour.advancedjava.services;

import edu.rseymour.advancedjava.model.StockData;
import edu.rseymour.advancedjava.model.StockQuote;
import edu.rseymour.advancedjava.util.DatabaseInitializationException;
import edu.rseymour.advancedjava.util.Interval;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the DatabaseStockService
 */
public class DatabaseStockServiceTest extends DatabaseServiceTest {

    private StockService databaseStockService;

    @Before
    public void setUp() throws DatabaseInitializationException {
        super.setUp();
        databaseStockService = ServiceFactory.getStockService();
    }

    @Test
    public void testGetQuote() throws Exception {
        String symbol = "APPL";
        StockQuote stockQuote = databaseStockService.getQuote(symbol);
        assertNotNull("Verify we can get a stock quote from the db", stockQuote);
        assertEquals("Make sure the symbols match", symbol, stockQuote.getSymbol());
    }

    @Test
    public void testGetQuoteWithIntervalBasic() throws Exception {
        String symbol = "GOOG";
        String fromStringDate = "2000-02-10 00:00:01";
        String untilStringDate = "2015-02-03 00:00:01";

        Calendar fromCalendar = makeCalendarFromString(fromStringDate);
        Calendar untilCalendar = makeCalendarFromString(untilStringDate);

        List<StockQuote> stockQuotes = databaseStockService.getQuote(symbol, fromCalendar, untilCalendar, Interval.DAY);

        assertFalse("verify stock quotes where returned", stockQuotes.isEmpty());
    }

    @Test
    public void testGetQuoteWithinRangeDay() throws Exception {

        String fromDateString = "2015-02-09 00:01:01";
        String endDateString = "2015-02-11 01:08:01";
        String symbol = "AMZN";

        Calendar fromCalendar = makeCalendarFromString(fromDateString);
        Calendar untilCalendar = makeCalendarFromString(endDateString);

        List<StockQuote> stockQuotes = databaseStockService.getQuote(symbol, fromCalendar, untilCalendar, Interval.DAY);
        assertEquals("got back expected number of stockquotes for one day interval", 4, stockQuotes.size());

        stockQuotes = databaseStockService.getQuote(symbol, fromCalendar, untilCalendar, Interval.MINUTE);
        assertEquals("got back expected number of stockquotes for one minute interval", 4, stockQuotes.size());
    }

    @Test
    public void testGetQuoteWithinRangeMinute() throws Exception {

        String fromDateString = "2015-02-10 00:02:01";
        String endDateString = "2015-02-10 00:04:01";
        String symbol = "AMZN";

        Calendar fromCalendar = makeCalendarFromString(fromDateString);
        Calendar untilCalendar = makeCalendarFromString(endDateString);

        List<StockQuote> stockQuotes =
                databaseStockService.getQuote(symbol, fromCalendar, untilCalendar, Interval.MINUTE);
        assertEquals("got back expected number of stockquotes for one minute interval", 3, stockQuotes.size());
    }

    /**
     * Handy dandy helper method that converts Strings in the format of   StockData.dateFormat
     * to Calendar instances set to the date expressed in the string.
     *
     * @param dateString a data and time in this format: StockData.dateFormat
     * @return a calendar instance set to the time in the string.
     * @throws ParseException if the string is not in the correct format, we can't tell what
     *                        time it is, and therefore can't make a calender set to that time.
     */
    private Calendar makeCalendarFromString(String dateString) throws ParseException {
        DateFormat format = new SimpleDateFormat(StockData.dateFormat, Locale.ENGLISH);
        Date date = format.parse(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }
}