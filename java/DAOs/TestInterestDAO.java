package DAOs;

import BB.Account;
import BB.Event;
import BB.SQLTablesManager;
import BB.Ticket;
import org.testng.annotations.Test;

import javax.validation.constraints.AssertTrue;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import static org.testng.AssertJUnit.*;

public class TestInterestDAO {
    @Test
    public void testIsAddRemove() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        InterestDAO interestDAO = dbManager.getInterestDAO();
        Account acc = new Account("vinme", "", "", "", 0);
        Event ev = new Event("ivent", "", null, null, null, null, 0);

        dbManager.getAccDAO().addAccount(acc.getUsername(), acc.getImage(), acc.getPhoneNumber(), acc.getPasswordHash());
        dbManager.getEventDAO().addevent(ev.getEvent_name(), 0, null, null, null, null, null, 0);

        assertTrue(!interestDAO.isinterested(acc.getUsername(), ev.getEvent_name()));
        interestDAO.AddInterest(acc, ev.getEvent_name());
        assertTrue(interestDAO.isinterested(acc.getUsername(), ev.getEvent_name()));
        interestDAO.RemoveInterest(acc, ev.getEvent_name());
        assertTrue(!interestDAO.isinterested(acc.getUsername(), ev.getEvent_name()));

        dbManager.getAccDAO().removeAccount(acc.getUsername());
        dbManager.getEventDAO().removeevent(ev.getEvent_name());
    }

    @Test
    public void TestGetInterests() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        InterestDAO interestDAO = dbManager.getInterestDAO();
        Account acc = new Account("vinme", "", "", "", 0);
        dbManager.getAccDAO().addAccount(acc.getUsername(), acc.getImage(), acc.getPhoneNumber(), acc.getPasswordHash());
        for (int i = 0; i < 20; i++) {
            Event ev = new Event("ivent"+i, "", null, null, null, null, 0);
            dbManager.getEventDAO().addevent(ev.getEvent_name(), 0, null, null, null, null, null, 0);
            interestDAO.AddInterest(acc, ev.getEvent_name());
        }
        ArrayList<Event> evs = interestDAO.getInterestedEvents(acc.getUsername());
        assertEquals(20, evs.size());
        for (int i = 0; i < 20; i++) {
            assertTrue(evs.get(i).getEvent_name().equals("ivent"+i));
        }
        dbManager.getAccDAO().removeAccount(acc.getUsername());
        for (int i = 0; i < 20; i++) {
            dbManager.getEventDAO().removeevent("ivent"+i);
        }
    }
}
