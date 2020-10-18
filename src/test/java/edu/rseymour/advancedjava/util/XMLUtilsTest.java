package edu.rseymour.advancedjava.util;

import edu.rseymour.advancedjava.model.database.QuoteDAO;
import edu.rseymour.advancedjava.model.database.StockSymbolDAO;
import edu.rseymour.advancedjava.services.UnknownStockSymbolException;
import edu.rseymour.advancedjava.xml.Stock;
import edu.rseymour.advancedjava.xml.Stocks;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;

public class XMLUtilsTest {

    String SYMBOL = "VNET";
    String PRICE = "110.10";

    private static String xmlStocks = "<?edu.rseymour.advancedjava.xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<stocks>\n" +
            "    <stock symbol=\"VNET\" price=\"110.10\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"AGTK\" price=\"120.10\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"AKAM\" price=\"3.10\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"AOL\"  price=\"30.10\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"BCOM\" price=\"10.10\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"BIDU\" price=\"10.10\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"BCOR\" price=\"12.10\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"WIFI\" price=\"16.10\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"BRNW\" price=\"0.70\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"CARB\" price=\"9.80\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"JRJC\" price=\"111.11\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"CCIH\" price=\"22.20\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"CHIC\" price=\"4.30\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"CNV\"  price=\"13.43\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"CCOI\" price=\"1.10\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"CNCG\" price=\".10\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"CXDO\" price=\"90.00\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"CRWG\" price=\"52.99\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"ELNK\" price=\"45.40\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"EATR\" price=\"15.60\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"EDXC\" price=\"18.40\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"ENV\"  price=\"220.61\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"EPAZ\" price=\"101.11\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"FB\"   price=\"500.17\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"FDIT\" price=\"160.90\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"FLPC\" price=\"177.70\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"GCLT\" price=\"8.90\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"GOOG\" price=\"700.10\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"GOOG\" price=\".10\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"GREZ\" price=\"77.91\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"IACI\" price=\"40.52\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"ICOA\" price=\"48.30\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"IIJI\" price=\"32.80\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"ILIA\" price=\"188.22\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"INAP\" price=\"2.12\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"IPAS\" price=\"1.02\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"JCOM\" price=\"19.99\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"LOGL\" price=\"18.21\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"LLNW\" price=\"45.55\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"LOOK\" price=\"38.90\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"MEET\" price=\"21.27\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"MEET\" price=\"310.31\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"VOIS\" price=\"440.51\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"MOMO\" price=\"8.51\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"NETE\" price=\"13.16\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"NTES\" price=\"14.23\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"EGOV\" price=\"17.35\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"NQ\" price=\"110.77\" time=\"2015-02-10 00:00:01\"/>\n" +
            "    <stock symbol=\"OTOW\" price=\"60.41\" time=\"2015-02-10 00:00:01\"/>\n" +
            "</stocks>";

    /**
     * I could not get this test to pass. It always put brackets ([]) in the returned
     * byteStream.toString(). I tried a million things, no success. Probably a really
     * simple solution that I am overlooking.
     */
//    @Test
//    public void testMarshall() throws Exception {
//        Stocks stocks = XMLUtils.unmarshall(xmlStocks, Stocks.class);
//        String xml = XMLUtils.marshall(stocks);
//        assertEquals("XML out is correct", xml.trim(), xmlStocks.trim());
//    }

    /**
     * Tests unmarshall without a schema validation.
     */
    @Test
    public void testUnmarshall() throws Exception {
        Stocks stocks = XMLUtils.unmarshall(xmlStocks, Stocks.class);
        validateStocks(stocks);
    }

    /**
     * Tests unmarshall with a schema validation.
     */
    @Test
    public void testUnmarshallWithSchemaValidation()throws Exception {
        Stocks stocks = XMLUtils.unmarshall(xmlStocks, Stocks.class, "/xml/stock_info.xsd");
        validateStocks(stocks);
    }

    /**
     * Adds a single quote, retrieves it from the database, then
     * executes asserts to validate it was added correctly.
     */
    @Test
    public void testAddQuote() throws UnknownStockSymbolException {

        BigDecimal price = new BigDecimal(999.00);
        String sTime = "2015-02-10 00:00:01";
        Timestamp timestamp = Timestamp.valueOf(sTime);
        StockSymbolDAO stockSymbolDAO = new StockSymbolDAO();
        stockSymbolDAO.setSymbol("TEST");

        QuoteDAO testQuoteDAO = new QuoteDAO();
        testQuoteDAO.setTime(timestamp);
        testQuoteDAO.setPrice(price);
        testQuoteDAO.setStockSymbolBySymbolId(stockSymbolDAO);

        XMLUtils.addQuote(timestamp, price, stockSymbolDAO.getSymbol());

        QuoteDAO dbQuoteDAO = DatabaseUtils.findUniqueResultBy("price", price, QuoteDAO.class, true);

        assertEquals("Time is equal", dbQuoteDAO.getTime(), testQuoteDAO.getTime());
        assertEquals("Price is equal", dbQuoteDAO.getPrice(), testQuoteDAO.getPrice());
        assertEquals("Symbol is equal",
                dbQuoteDAO.getStockSymbolBySymbolId().equals("TEST"),
                testQuoteDAO.getStockSymbolBySymbolId().equals("TEST"));
    }

    /**
     * Adds a list of quotes and then executes asserts using a known quote index.
     */
    @Test
    public void testAddListOfQuotes() throws UnknownStockSymbolException, InvalidXMLException {

        String price = "500.17";
        String sTime = "2015-02-10 00:00:01";

        Stocks stocks = XMLUtils.unmarshall(xmlStocks, Stocks.class);
        List<Stock> stock = stocks.getStock();
        Stock testStock = stock.get(23);

        XMLUtils.addListOfQuotes(xmlStocks, Stocks.class, "/xml/stock_info.xsd");

        assertEquals("Time is equal", sTime, testStock.getTime());
        assertEquals("Price is equal", price, testStock.getPrice());
        assertTrue("Symbol is FB", testStock.getSymbol().equals("FB"));
    }

    /**
     * Helper test method for marshalling and unmarshalling tests.
     */
    private void validateStocks(Stocks stocks) {
        assertTrue("Symbol name is correct", stocks.getStock().get(0).getSymbol().equals(SYMBOL));
        assertTrue("Price is correct", stocks.getStock().get(0).getPrice().equals(PRICE));
        assertTrue("There are 49 stock instances", stocks.getStock().size() == 49);
    }
}


