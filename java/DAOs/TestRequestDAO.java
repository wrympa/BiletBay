package DAOs;

import BB.SQLTablesManager;
import org.junit.jupiter.api.Test;

import static org.testng.AssertJUnit.*;

public class TestRequestDAO {
    @Test
    public void testSendRejectDeleteContains() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        RequestDAO requestDAO = dbManager.getRequestDAO();

        assertTrue(requestDAO.getAllAccounts().size() >= 1);

        /** Test Send Requests */
        requestDAO.sendRequest("user1", "user2", "ticket1");
        requestDAO.sendRequest("user2", "user1", "ticket2");
        requestDAO.sendRequest("user1", "user3", "ticket3");

        assertEquals(4, requestDAO.getAllAccounts().size());

        /** Test Contains */
        assertTrue(requestDAO.containsRequest("user1", "user2", "ticket1"));
        assertTrue(!requestDAO.containsRequest("user3", "user1", "ticket3"));
        assertTrue(requestDAO.containsRequest("user2", "user1", "ticket2"));

        /** Test Reject Requests */
        requestDAO.rejectRequest("user2", "user1", "ticket2");
        assertTrue(!requestDAO.containsRequest("user2", "user1", "ticket2"));
        assertEquals(3, requestDAO.getAllAccounts().size());

        /** Test Delete Requests */

        requestDAO.sendRequest("user3", "user2", "ticket1");
        requestDAO.sendRequest("user4", "user2", "ticket1");
        requestDAO.sendRequest("user5", "user2", "ticket1");

        assertTrue(requestDAO.containsRequest("user1", "user2", "ticket1"));
        assertTrue(requestDAO.containsRequest("user3", "user2", "ticket1"));
        assertTrue(requestDAO.containsRequest("user4", "user2", "ticket1"));
        assertTrue(requestDAO.containsRequest("user5", "user2", "ticket1"));

        requestDAO.deleteRequests("ticket1");

        assertTrue(!requestDAO.containsRequest("user1", "user2", "ticket1"));
        assertTrue(!requestDAO.containsRequest("user3", "user2", "ticket1"));
        assertTrue(!requestDAO.containsRequest("user4", "user2", "ticket1"));
        assertTrue(!requestDAO.containsRequest("user5", "user2", "ticket1"));

    }

    @Test
    public void testGetRequests() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        RequestDAO requestDAO = dbManager.getRequestDAO();

        requestDAO.sendRequest("user3", "user8", "ticket1");
        requestDAO.sendRequest("user4", "user8", "ticket1");
        requestDAO.sendRequest("user5", "user8", "ticket1");
        requestDAO.sendRequest("user5", "user8", "ticket7");

        assertNotNull(requestDAO.getRequests("user8"));
        assertEquals(4, requestDAO.getRequests("user8").size());
    }
}
