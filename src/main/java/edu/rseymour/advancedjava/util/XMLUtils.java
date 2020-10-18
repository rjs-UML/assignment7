package edu.rseymour.advancedjava.util;

import edu.rseymour.advancedjava.model.database.QuoteDAO;
import edu.rseymour.advancedjava.model.database.StockSymbolDAO;
import edu.rseymour.advancedjava.services.UnknownStockSymbolException;
import edu.rseymour.advancedjava.xml.Stock;
import edu.rseymour.advancedjava.xml.Stocks;
import edu.rseymour.advancedjava.xml.XMLDomainObject;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XMLUtils {

    /**
     * Takes Java objects, converts them into a byteStream, formats them into XML using the
     * model class passed as a param, then returns the byteStream as a String
     *
     * @param domainClass the XML model class.
     * @return a String that is an XML instance for the domain class parameter
     * @throws InvalidXMLException if the object cannot be parsed into XML
     */
    public static String marshall(XMLDomainObject domainClass) throws InvalidXMLException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {

            JAXBContext context = JAXBContext.newInstance(domainClass.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(domainClass, byteArrayOutputStream);

        } catch (JAXBException e) {
            throw new InvalidXMLException(e.getMessage(), e);
        }
        return byteArrayOutputStream.toString();
    }

    /**
     * Takes a String version of an XML file and turns it into an object that
     * has a list of XML elements as an attribute.
     *
     * @param xmlInstance an XML instance that matches the XML Domain Object specified by T
     * @param T           an XML Domain Object class that corresponds to the XML instance
     * @return XML Domain Object of type T that is populated with values in the provided String
     * @throws InvalidXMLException if the provided xmlInstance cannot be successfully parsed
     */
    public static <T> T unmarshall(String xmlInstance, Class T) throws InvalidXMLException {
        T returnValue;
        try {
            Unmarshaller unmarshaller = createUnmarshaller(T);
            returnValue = (T) unmarshaller.unmarshal(new StringReader(xmlInstance));
        } catch (JAXBException e) {
            throw new InvalidXMLException(e.getMessage(), e);
        }
        return returnValue;
    }


    /**
     * Takes a String version of an XML file and turns it into an object that
     * has a list of XML elements as an attribute.
     * <p>
     * Uses schema validation.
     *
     * @param xmlInstance an XML instance that matches the XML Domain Object specified by T
     * @param T           an XML Domain Object class that corresponds to the XML instance
     * @return XML Domain Object of type T that is populated with values in the provided String
     * @throws InvalidXMLException if the provided xmlInstance cannot be successfully parsed
     */
    public static <T> T unmarshall(String xmlInstance, Class T, String schemaName)
            throws InvalidXMLException {

        T returnValue;
        try {
            InputStream resourceAsStream = XMLUtils.class.getResourceAsStream(schemaName);
            Source schemaSource = new StreamSource(resourceAsStream);
            if (resourceAsStream == null) {
                throw new IllegalStateException("Schema: " + schemaName + " on classpath. " +
                        "Could not validate input XML");
            }
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(schemaSource);
            Unmarshaller unmarshaller = createUnmarshaller(T);
            unmarshaller.setSchema(schema);

            returnValue = (T) unmarshaller.unmarshal(new StringReader(xmlInstance));
        } catch (JAXBException | SAXException e) {
            throw new InvalidXMLException(e.getMessage(), e);
        }
        return returnValue;
    }

    /**
     * Adds a single quote to the database. Calls <CODE>addSymbol</CODE> if the symbol
     * is not currently in the database.
     *
     * @param time    the timestamp of the quote
     * @param price   the price of the quote
     * @param symbol  the symbol for the quote
     * @throws UnknownStockSymbolException if the stock symbol is invalid
     */
    public static void addQuote(Timestamp time, BigDecimal price, String symbol)
            throws UnknownStockSymbolException {
        StockSymbolDAO stockSymbolDAO;
        // check the db to see if the stock
        stockSymbolDAO = DatabaseUtils.findUniqueResultBy("symbol", symbol, StockSymbolDAO.class, true);
        if (stockSymbolDAO == null) {
            XMLUtils.addSymbol(symbol);
            stockSymbolDAO = DatabaseUtils.findUniqueResultBy("symbol", symbol, StockSymbolDAO.class, true);
        }

        QuoteDAO quoteDAO = new QuoteDAO();
        quoteDAO.setTime(time);
        quoteDAO.setPrice(price);
        quoteDAO.setStockSymbolBySymbolId(stockSymbolDAO);

        Transaction transaction = null;
        Session session = null;
        try {
            session = DatabaseUtils.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(quoteDAO);
            transaction.commit();

        } catch (ConstraintViolationException e) {
            throw new UnknownStockSymbolException(e.getMessage(), e);
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new UnknownStockSymbolException(e.getMessage(), e);
        } finally {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Adds a list of stocks to the database. This method unmarshalls a string of stock
     * into any model class that contains a List of stocks as an attribute.
     *
     * @param xmlInstance
     * @param T
     * @param schemaName
     * @throws InvalidXMLException
     * @throws UnknownStockSymbolException
     */
    public static void addListOfQuotes(String xmlInstance, Class T, String schemaName)
            throws InvalidXMLException, UnknownStockSymbolException {
        Stocks stocks = XMLUtils.unmarshall(xmlInstance, Stocks.class, schemaName);
        List<Stock> stockList = stocks.getStock();
        for (Stock stock : stockList) {
            String symbol = stock.getSymbol();
            Timestamp timestamp = Timestamp.valueOf(stock.getTime());
            BigDecimal price = new BigDecimal(stock.getPrice());
            addQuote(timestamp, price, symbol);
        }
    }

    /**
     * Helper method to create an unmarshaller instance
     *
     * @param T an XML domain object that corresponds to the XML String being
     *          parsed into an XML Domain Object
     * @return an unmarshaller instance
     * @throws JAXBException if the provided class cannot be used to create an unmarshaller
     */
    private static Unmarshaller createUnmarshaller(Class T) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(T);
        return jaxbContext.createUnmarshaller();

    }

    /**
     * Helper method that adds a symbol to the stock_symbol table.
     *
     * @param symbol the name of the symbol to add
     * @throws UnknownStockSymbolException
     */
    public static void addSymbol(String symbol) throws UnknownStockSymbolException {
        StockSymbolDAO stockSymbolDAO = new StockSymbolDAO();
        stockSymbolDAO.setSymbol(symbol);

        Transaction transaction = null;
        Session session = null;

        try {
            session = DatabaseUtils.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(stockSymbolDAO);
            transaction.commit();
        } catch (ConstraintViolationException e) {
            throw new UnknownStockSymbolException(e.getMessage(), e);
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new UnknownStockSymbolException(e.getMessage(), e);
        } finally {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            if (session != null) {
                session.close();
            }
        }
    }
}
