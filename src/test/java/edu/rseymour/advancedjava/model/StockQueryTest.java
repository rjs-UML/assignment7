package edu.rseymour.advancedjava.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for StockQuery Class
 */
public class StockQueryTest {

    @Test
    public void testBasicConstruction() throws Exception{
        String symbol = "APPL";
        StockQuery stockQuery = new StockQuery(symbol,"2011-10-29 12:12:1","2014-10-29 12:12:1");
        assertEquals("Verify construction", symbol, stockQuery.getSymbol());
    }

}