package DAOs;

import BB.Account;
import BB.Event;
import BB.SQLTablesManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.*;
import static org.testng.AssertJUnit.assertEquals;


public class TestEventDAO {
    @Test
    public void testGetSearchEvents() throws Exception {
        /**Test First and Separately Before adding new elements to the table */
        SQLTablesManager dbManager = new SQLTablesManager("test");
        EventDAO eventDAO = dbManager.getEventDAO();

        assertEquals(4, eventDAO.search_event("r", "event_date", "descending").size());
        assertEquals(1, eventDAO.search_event("dra", "", "").size());
        assertEquals(0, eventDAO.search_event("aravin", "", "").size());
        assertEquals(4, eventDAO.search_event("A", "", "").size());

        ArrayList<Event> found = (ArrayList<Event>) eventDAO.search_event("barbie", "", "");

        Event event = found.get(0);
        assertEquals("2023-07-20", event.getEvent_date());
        assertEquals("cinema", event.getEvent_category());
        assertEquals("barbis premiera", event.getEvent_description());
        assertEquals("barbie", event.getEvent_name());
        assertEquals("barbie.jpg", event.getEvent_picture());
        assertEquals("20:00:00", event.getEvent_time());

    }

    @Test
    public void testGetAllEvents() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        EventDAO eventDAO = dbManager.getEventDAO();
        assertNotNull(eventDAO.GetAllEvents());
        assertTrue(eventDAO.GetAllEvents().size() > 0);
        assertTrue(eventDAO.GetAllEvents().size() >= 7);
    }

    @Test
    public void testAddRemoveGetByName() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        EventDAO eventDAO = dbManager.getEventDAO();

        int curNum = eventDAO.GetAllEvents().size();
        assertNull(eventDAO.GetEventByName("ararsebuli eventi"));
        /** Test Add Account */
        eventDAO.addevent("eve1", 123, "2024-02-03", "00:00:00", "cat", "immg", "descr", 1);
        eventDAO.addevent("eve2", 123, "2024-02-04", "00:00:00", "cat", "immg", "descr", 1);
        curNum += 2;
        assertEquals(curNum, eventDAO.GetAllEvents().size());
        assertTrue(eventDAO.GetEventByName("eve1") != null);
        assertTrue(eventDAO.GetEventByName("eve2") != null);

        /** Test Remove Account */
        eventDAO.removeevent("eve2");
        curNum--;
        assertTrue(eventDAO.GetEventByName("eve2") == null);
        assertEquals(curNum, eventDAO.GetAllEvents().size());
    }


    @Test
    public void testEventByCategories() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        EventDAO eventDAO = dbManager.getEventDAO();

        eventDAO.addevent("ev1", 123, "2023-08-08", "10:00", "categ", "immg", "descr", 1);
        eventDAO.addevent("ev2", 123, "2023-08-08", "10:00", "categ", "immg", "descr", 1);
        List<Event> evs = eventDAO.events_of_category("categ", "event_date", "descending");
        ArrayList<String> evstr1 = new ArrayList<>();
        evstr1.add("ev1");
        evstr1.add("ev2");
        ArrayList<String> evstr2 = new ArrayList<>();
        evstr2.add(evs.get(0).getEvent_name());
        evstr2.add(evs.get(1).getEvent_name());
        assertEquals(evstr1, evstr2);

    }


    @Test
    public void testTop3() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        EventDAO eventDAO = dbManager.getEventDAO();
        AccountDAO accountDAO = dbManager.getAccDAO();
        InterestDAO interestDAO = dbManager.getInterestDAO();

        eventDAO.addevent("ev1", 1000001, "2023-08-08", "10:00", "cat", "immg", "descr", 1);
        eventDAO.addevent("ev2", 1000002, "2023-08-08", "10:00", "cat", "immg", "descr", 1);
        eventDAO.addevent("ev3", 1000003, "2023-08-08", "10:00", "cat", "immg", "descr", 1);
        for (int i = 0; i < 20; i++) {
            Account acc = new Account(i+"a", "","","",0);
            interestDAO.AddInterest(acc, "ev1");
            interestDAO.AddInterest(acc, "ev2");
            interestDAO.AddInterest(acc, "ev3");
        }
        List<Event> evs = eventDAO.gettop3();
        ArrayList<String> evstr1 = new ArrayList<>();
        evstr1.add("ev3");
        evstr1.add("ev2");
        evstr1.add("ev1");
        ArrayList<String> evstr2 = new ArrayList<>();
        evstr2.add(evs.get(2).getEvent_name());
        evstr2.add(evs.get(1).getEvent_name());
        evstr2.add(evs.get(0).getEvent_name());
        assertEquals(evstr1, evstr2);
        for (int i = 0; i < 20; i++) {
            Account acc = new Account(i+"a", "","","",0);
            interestDAO.RemoveInterest(acc, "ev1");
            interestDAO.RemoveInterest(acc, "ev2");
            interestDAO.RemoveInterest(acc, "ev3");
        }
    }


    @Test
    public void testDecrementIncrementInterest() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        EventDAO eventDAO = dbManager.getEventDAO();
        eventDAO.addevent("ev", 123, "2023-09-09", "11:11", "cat", "immg", "descr", 1);

        int pop = 123;
        pop = pop + 1;
        Event e = eventDAO.GetEventByName("ev");
    }

    @Test
    public void testGetUnapprovedEvents() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        EventDAO eventDAO = dbManager.getEventDAO();

        List<Event> events = eventDAO.getUnapprovedEvents();
        assertEquals(2, events.size());
        assertEquals("Arctic Monkeys Concert", events.get(0).getEvent_name());
        assertEquals("20:00:00", events.get(0).getEvent_time());
        assertEquals("2023-08-07", events.get(0).getEvent_date());
        assertEquals("default.png", events.get(0).getEvent_picture());
        assertEquals("yvelaze slei koncerti", events.get(0).getEvent_description());

        assertEquals("The Strokes Concert", events.get(1).getEvent_name());
//        assertEquals("The Strokes Concert", events.get(1).getEvent_name());
//        assertEquals("The Strokes Concert", events.get(1).getEvent_name());
//        assertEquals("The Strokes Concert", events.get(1).getEvent_name());
//        assertEquals("The Strokes Concert", events.get(1).getEvent_name());
//        assertEquals("The Strokes Concert", events.get(1).getEvent_name());
    }


    @Test
    public void testGetSortedEvents() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        EventDAO eventDAO = dbManager.getEventDAO();
        InterestDAO interestDAO = dbManager.getInterestDAO();
        eventDAO.addevent("teste3", 3, "2021-09-09", "11:11", "cat", "immg", "descr", 1);
        eventDAO.addevent("teste2", 2, "2022-09-09", "11:11", "cat", "immg", "descr", 1);
        eventDAO.addevent("teste1", 1, "2021-11-09", "11:11", "cat", "immg", "descr", 1);
        for (int i = 0; i < 20; i++) {
            Account acc = new Account(i+"a", "","","",0);
            interestDAO.AddInterest(acc, "teste1");
            interestDAO.AddInterest(acc, "teste2");
            interestDAO.AddInterest(acc, "teste3");
        }
        for (int i = 20; i < 30; i++) {
            Account acc = new Account(i+"a", "","","",0);
            interestDAO.AddInterest(acc, "teste2");
            interestDAO.AddInterest(acc, "teste3");
        }
        for (int i = 30; i < 35; i++) {
            Account acc = new Account(i+"a", "","","",0);
            interestDAO.AddInterest(acc, "teste3");
        }
        List<Event> events = eventDAO.getSortedEvents("popularity", "descending");
        assertEquals("teste3", events.get(0).getEvent_name());
        assertEquals("teste2", events.get(1).getEvent_name());
        assertEquals("teste1", events.get(2).getEvent_name());
        assertEquals(35, eventDAO.getEvent_popularity("teste3"));

    }

    @Test
    public void testApproveEvent() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        EventDAO eventDAO = dbManager.getEventDAO();

        eventDAO.addevent("appr1", 800, "2021-09-09", "11:11", "cat", "immg", "descr", 0);
        Event appr1 = eventDAO.GetEventByName("appr1");
        assertFalse(appr1.getApproved());
        eventDAO.approveEvent("appr1");
        appr1.setApproved();
        assertTrue(appr1.getApproved());


    }
}