package BB;
import DAOs.AccountDAO;
import DAOs.EventDAO;
import DAOs.InterestDAO;
import org.apache.commons.dbcp2.BasicDataSource;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.validation.constraints.AssertTrue;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class Test_Account {
    @Test
    public void testSample1() {
        Account account = new Account("Ana", "555555555", "default.jpg", "pass", 0);
        assertEquals("Ana", account.getUsername());
        assertEquals("555555555", account.getPhoneNumber());
        assertEquals("9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684", account.getPasswordHash());
        assertEquals("default.jpg", account.getImage());
        assertTrue(!account.checkPassToChange("pas"));
        account.changePassword("pass", "12345678");
        assertEquals("7c222fb2927d828af22f592134e8932480637c0d", account.getPasswordHash());
        account.changeUsername("annaa");
        assertEquals("annaa", account.getUsername());
        account.changePhoneNumber("568568568");
        assertEquals("568568568", account.getPhoneNumber());
        account.changeImage("path/newImage");
        assertEquals("path/newImage", account.getImage());
        account.removeImage();
        assertEquals("default.jpg", account.getImage());
        account.setPasswordHash("9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684");
        assertEquals("9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684", account.getPasswordHash());
    }

    @Test
    public void testSample2() {
        Account acc = new Account("user", "515151515", "", "8765", 0);
        Ticket ticket = new Ticket("1", "", "", "", "", "", 0, "", "", "", "", null, null);
        acc.addTicket(ticket);
        assertEquals(1, acc.getTickets().size());
        acc.removeTicket("1");
        assertEquals(0, acc.getTickets().size());
    }

    @Test
    public void testSample3() throws Exception {
        BasicDataSource stream = new BasicDataSource();
        stream.setUrl("jdbc:mysql://localhost:3306/test");
        stream.setUsername("root");
        stream.setPassword("Dati2003619");


        Account acc = new Account("user", "515151515", "", "8765", 1);
        Event event = new Event("kargi event", "", "desc", null, null, "", 1);

        EventDAO eventDAO = new EventDAO(stream);
        InterestDAO interestDAO = new InterestDAO(stream, eventDAO);
        eventDAO.addevent(event.getEvent_name(), 0, event.getEvent_date(), event.getEvent_time(), event.getEvent_category(), event.getEvent_picture(), event.getEvent_description(), 1);

        interestDAO.AddInterest(acc, event.getEvent_name());
        assertEquals(1, interestDAO.getInterestedEvents(acc.getUsername()).size());
        interestDAO.RemoveInterest(acc, event.getEvent_name());
        assertEquals(0, interestDAO.getInterestedEvents(acc.getUsername()).size());

        assertTrue(acc.isModerator());
        Event event1 = new Event("cudi event", "", "desc", null, null, "", 1);
        eventDAO.addevent(event1.getEvent_name(), 0, event1.getEvent_date(), event1.getEvent_time(), event1.getEvent_category(), event1.getEvent_picture(), event1.getEvent_description(), 1);

        interestDAO.AddInterest(acc, event1.getEvent_name());
        assertTrue(interestDAO.isinterested(acc.getUsername(), event1.getEvent_name()));
        assertTrue(!interestDAO.isinterested(acc.getUsername(), event.getEvent_name()));
        eventDAO.removeevent(event1.getEvent_name());
        eventDAO.removeevent(event.getEvent_name());
    }
}
