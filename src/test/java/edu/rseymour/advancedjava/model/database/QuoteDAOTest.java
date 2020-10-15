package edu.rseymour.advancedjava.model.database;

import edu.rseymour.advancedjava.util.DatabaseUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 *  Verify the QuoteDAO class
 */
public class QuoteDAOTest extends AbstractBaseDAOTest {

    @Test
    public void testRead() {
        QuoteDAO quoteDAO = DatabaseUtils.findUniqueResultBy("id", 1, QuoteDAO.class, true);
        assertTrue("first quoteDAO found", quoteDAO.getId() == 1);
    }
}
