package BB;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class TestRequest {
    @Test
    public void testSample1() {
        Request request = new Request("ana", "Dream", "dufyg98560");
        assertEquals("ana", request.getSentBy());
        assertEquals("Dream", request.getSentTo());
        assertEquals("dufyg98560", request.getTicketId());
    }

    @Test
    public void testSample2() {
        Account acc1 = new Account("user1", "509090909", "default.jpg", "1234", 0);
        Account acc2 = new Account("user2", "509090900", "default.jpg", "paroli", 0);
        Ticket ticket = new Ticket("jdgbl08766", "", "", "", "", "", 0, "", "", "", "", null, null);
        Request request = new Request(acc1.getUsername(), acc2.getUsername(), ticket.getTicket_id());
        assertEquals("user1", request.getSentBy());
        assertEquals("user2", request.getSentTo());
        assertEquals("jdgbl08766", request.getTicketId());
    }

}
