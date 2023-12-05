package DAOs;

import BB.Ticket;
import BB.SQLTablesManager;
import org.testng.annotations.Test;

import java.sql.Date;
import java.sql.Time;

import static org.testng.AssertJUnit.*;

public class TestTicketDAO{
    @Test
    public void test1() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        TicketDAO ticketDAO = dbManager.getTickDAO();

        Ticket t = ticketDAO.getticketbyname("jhfbq32489");

        assertEquals("jhfbq32489", t.getTicket_id());
        assertEquals("barbie", t.getEvent_name());
        assertEquals("Dream", t.getPosted_by());
        assertEquals("2023-06-23", t.getPost_date());
        assertEquals("null", t.getBought_by());
        assertEquals("cinema", t.getCategory());
        assertEquals(50.00, t.getPrice());
        assertEquals("barbis pirveli seansis 20 ivlibss, biletebi, row 1 seat 2 galeria 15:10", t.getDescription());
        assertEquals("tickpick2.jpg", t.getImage());
        assertEquals("buy", t.getType());
        assertEquals("null", t.getCurrBuyer());
        assertEquals(null, t.getAuctDate());
        assertEquals(null, t.getAuctTime());
    }

    @Test
    public void test2() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        TicketDAO ticketDAO = dbManager.getTickDAO();

        assertTrue(ticketDAO.exists("fasfw74281"));
        assertFalse(ticketDAO.exists("1234567890"));
        assertTrue(ticketDAO.exists("jhfbq32490"));

        assertEquals(1, ticketDAO.tickets_bought_by("GSagh").size());
        assertEquals(11, ticketDAO.tickets_bought_by("null").size());
        assertEquals(0, ticketDAO.tickets_bought_by("Dream").size());

        assertEquals(0, ticketDAO.tickets_posted_by("null").size());
        assertEquals(11, ticketDAO.tickets_posted_by("Dream").size());
        assertEquals(0, ticketDAO.tickets_posted_by("user1").size());
    }

    @Test
    public void test3() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        TicketDAO ticketDAO = dbManager.getTickDAO();

        assertEquals(2, ticketDAO.getAllByEventName("barbie").size());
        assertEquals(0, ticketDAO.getAllByEventName("imagine dragons 2023").size());
        assertEquals(4, ticketDAO.getAllByEventName("uefa u21 championship finale").size());
        assertEquals(1, ticketDAO.getAllByEventName("bruno mars 2023").size());


        assertEquals(1, ticketDAO.getsoldcount());
    }

    @Test
    public void test4() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        TicketDAO ticketDAO = dbManager.getTickDAO();

        assertEquals(0, ticketDAO.tickets_posted_by("user1").size());

        ticketDAO. addticket("1234567890","barbie", "2023-07-20", "user1",
                "null", "cinema", 25, "Let's go party", "1234567890.jpg",
                "buy", "null", null, null);
        assertEquals(3, ticketDAO.getAllByEventName("barbie").size());
        ticketDAO. addticket("1234567891","barbie", "2023-07-20", "user2",
                "user3", "cinema", 25, "Let's go party", "1234567890.jpg",
                "buy", "null", null, null);
        assertEquals(3, ticketDAO.getAllByEventName("barbie").size());

        assertEquals(1, ticketDAO.tickets_posted_by("user1").size());
        assertEquals(1, ticketDAO.tickets_bought_by("user3").size());

        assertTrue(ticketDAO.exists("1234567890"));
        assertTrue(ticketDAO.exists("1234567891"));

        ticketDAO.removeticket("1234567890");
        ticketDAO.removeticket("1234567891");

        assertEquals(0, ticketDAO.tickets_posted_by("user1").size());
        assertEquals(0, ticketDAO.tickets_bought_by("user3").size());

        assertFalse(ticketDAO.exists("1234567890"));
        assertFalse(ticketDAO.exists("1234567891"));
    }

    @Test
    public void test5() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        TicketDAO ticketDAO = dbManager.getTickDAO();

        assertEquals(0, ticketDAO.tickets_bought_by("user1").size());
        ticketDAO.changetickettobought("bdhds83472", "user1");
        assertEquals(1, ticketDAO.tickets_bought_by("user1").size());
        ticketDAO.changetickettobought("bdhds83472", "null");
        assertEquals(0, ticketDAO.tickets_bought_by("user1").size());
    }

    @Test
    public void test6() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        TicketDAO ticketDAO = dbManager.getTickDAO();

        ticketDAO. addticket("1234567890","barbie", "2023-07-20", "user1",
                "null", "cinema", 25, "Let's go party", "1234567890.jpg",
                "auc", "null", new Date(2023, 8, 1), new Time(12, 0, 0));

        assertTrue(ticketDAO.exists("1234567890"));
        ticketDAO.placebid("1234567890", "user2", 25);

        Ticket t = ticketDAO.getticketbyname("1234567890");
        assertEquals("1234567890", t.getTicket_id());
        assertEquals("barbie", t.getEvent_name());
        assertEquals("user1", t.getPosted_by());
        assertEquals("2023-07-20", t.getPost_date());
        assertEquals("null", t.getBought_by());
        assertEquals("cinema", t.getCategory());
        assertEquals(25.00, t.getPrice());
        assertEquals("Let's go party", t.getDescription());
        assertEquals("1234567890.jpg", t.getImage());
        assertEquals("auc", t.getType());
        assertEquals("user2", t.getCurrBuyer());
        assertEquals(new Date(2023, 8, 1), t.getAuctDate());
        assertEquals(new Time(12, 0, 0), t.getAuctTime());

        ticketDAO.removeticket("1234567890");
        assertFalse(ticketDAO.exists("1234567890"));
    }

    @Test
    public void test7() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        TicketDAO ticketDAO = dbManager.getTickDAO();

        assertEquals(0, ticketDAO.getStatistics("uefa u21 championship finale", ""));
        assertEquals(1, ticketDAO.getStatistics("", "GSagh"));
        assertEquals(0, ticketDAO.getStatistics("uefa u21 championship finale", "user1"));
        assertEquals(0, ticketDAO.getStatistics("", "user1"));
        assertEquals(0, ticketDAO.getStatistics("", "user2"));

        ticketDAO.changetickettobought("bdhds83472", "user1");
        assertEquals(1, ticketDAO.getStatistics("uefa u21 championship finale", ""));
        assertEquals(1, ticketDAO.getStatistics("uefa u21 championship finale", "user1"));
        assertEquals(1, ticketDAO.getStatistics("", "user1"));
        assertEquals(0, ticketDAO.getStatistics("", "user2"));

        ticketDAO.changetickettobought("hgowl45394", "user1");
        ticketDAO.changetickettobought("bdhds83473", "user2");
        assertEquals(3, ticketDAO.getStatistics("uefa u21 championship finale", ""));
        assertEquals(2, ticketDAO.getStatistics("uefa u21 championship finale", "user1"));
        assertEquals(2, ticketDAO.getStatistics("", "user1"));
        assertEquals(1, ticketDAO.getStatistics("", "user2"));

        ticketDAO.changetickettobought("hgowl45394", "null");
        assertEquals(2, ticketDAO.getStatistics("uefa u21 championship finale", ""));
        assertEquals(1, ticketDAO.getStatistics("uefa u21 championship finale", "user1"));
        assertEquals(1, ticketDAO.getStatistics("", "user1"));
        assertEquals(1, ticketDAO.getStatistics("", "user2"));

        ticketDAO.changetickettobought("bdhds83473", "null");
        ticketDAO.changetickettobought("bdhds83472", "null");

        assertEquals(0, ticketDAO.getStatistics("uefa u21 championship finale", ""));
        assertEquals(0, ticketDAO.getStatistics("uefa u21 championship finale", "user1"));
        assertEquals(0, ticketDAO.getStatistics("", "user1"));
        assertEquals(0, ticketDAO.getStatistics("", "user2"));
    }

    @Test
    public void test8() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        TicketDAO ticketDAO = dbManager.getTickDAO();

        assertEquals(ticketDAO.getAllByEventName("oppenheimer").size(),
                ticketDAO.filter_sort("oppenheimer", 0, 10000, 0, "").size());

        assertEquals(4, ticketDAO.filter_sort("oppenheimer", 0, 10000, 1, "").size());
        assertEquals(3, ticketDAO.filter_sort("oppenheimer", 0, 50, 0, "").size());
        assertEquals(1, ticketDAO.filter_sort("oppenheimer", 10, 49, 0, "").size());
        assertEquals(2, ticketDAO.filter_sort("oppenheimer", 0, 49.50, 0, "").size());
        assertEquals(3, ticketDAO.filter_sort("oppenheimer", 49.50, 60, 0, "").size());
        assertEquals(2, ticketDAO.filter_sort("oppenheimer", 50, 55, 0, "").size());


        Ticket t1 = ticketDAO.filter_sort("oppenheimer", 0, 49.50, 0, "").get(0);
        Ticket t2 = ticketDAO.filter_sort("oppenheimer", 0, 49.50, 0, "").get(1);

        assertEquals(49.50, t1.getPrice());
        assertEquals(47.00, t2.getPrice());

        t1 = ticketDAO.filter_sort("oppenheimer", 0, 49.50, 1, "").get(0);
        t2 = ticketDAO.filter_sort("oppenheimer", 0, 49.50, 1, "").get(1);

        assertEquals(47.00, t1.getPrice());
        assertEquals(49.50, t2.getPrice());

        t1 = ticketDAO.filter_sort("oppenheimer", 0, 50, 2, "").get(0);
        t2 = ticketDAO.filter_sort("oppenheimer", 0, 50, 2, "").get(1);
        Ticket t3 = ticketDAO.filter_sort("oppenheimer", 0, 50, 2, "").get(2);

        assertEquals(50.00, t1.getPrice());
        assertEquals(49.50, t2.getPrice());
        assertEquals(47.00, t3.getPrice());

        assertEquals(2, ticketDAO.filter_sort("oppenheimer", 0, 10000, 1, "buy").size());
        assertEquals(2, ticketDAO.filter_sort("oppenheimer", 0, 10000, 0, "auc").size());
        assertEquals(1, ticketDAO.filter_sort("oppenheimer", 0, 49, 0, "auc").size());
        assertEquals(0, ticketDAO.filter_sort("oppenheimer", 0, 49, 0, "buy").size());
        assertEquals(1, ticketDAO.filter_sort("oppenheimer", 0, 49.50, 2, "buy").size());
        assertEquals(1, ticketDAO.filter_sort("oppenheimer", 0, 49.50, 2, "auc").size());
        assertEquals(2, ticketDAO.filter_sort("oppenheimer", 0, 49.50, 1, "").size());
        assertEquals(1, ticketDAO.filter_sort("oppenheimer", 47.50, 49.50, 1, "").size());
        assertEquals(1, ticketDAO.filter_sort("oppenheimer", 47.50, 49.50, 2, "buy").size());
        assertEquals(0, ticketDAO.filter_sort("oppenheimer", 47.50, 49.50, 2, "auc").size());
    }

    @Test
    public void test9() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        TicketDAO ticketDAO = dbManager.getTickDAO();

        ticketDAO.edit("jhfbq32489", "oppenheimer", "cinema", 30.20, "edited", "buy", null, null);
        Ticket t = ticketDAO.getticketbyname("jhfbq32489");

        assertEquals("jhfbq32489", t.getTicket_id());
        assertEquals("oppenheimer", t.getEvent_name());
        assertEquals("Dream", t.getPosted_by());
        assertEquals("2023-06-23", t.getPost_date());
        assertEquals("null", t.getBought_by());
        assertEquals("cinema", t.getCategory());
        assertEquals(30.20, t.getPrice());
        assertEquals("edited", t.getDescription());
        assertEquals("tickpick2.jpg", t.getImage());
        assertEquals("buy", t.getType());
        assertEquals("null", t.getCurrBuyer());
        assertNull(t.getAuctDate());
        assertNull(t.getAuctTime());

        ticketDAO.edit("jhfbq32489", "barbie", "cinema", 50.00,
                "barbis pirveli seansis 20 ivlibss, biletebi, row 1 seat 2 galeria 15:10", "buy", null, null);
    }

    @Test
    public void test10() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        TicketDAO ticketDAO = dbManager.getTickDAO();

        assertTrue(ticketDAO.userOwnsTicket("Dream", "oUbDa63954"));
        assertTrue(ticketDAO.userOwnsTicket("Dream", "fahiw74282"));
        assertTrue(ticketDAO.userOwnsTicket("Dream", "jhfbq32490"));
        assertTrue(ticketDAO.userOwnsTicket("Dream", "bdhds83473"));
        assertFalse(ticketDAO.userOwnsTicket("user1", "jhfbq32489"));
        assertFalse(ticketDAO.userOwnsTicket("user2", "hgowl45394"));
        assertFalse(ticketDAO.userOwnsTicket("user3", "mfdhu53543"));
    }

    @Test
    public void test11() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        TicketDAO ticketDAO = dbManager.getTickDAO();

        assertTrue(ticketDAO.tickets_sold_by("Dream").size() == 1);
        assertTrue(ticketDAO.tickets_sold_by("user1").size() == 0);
        assertTrue(ticketDAO.tickets_sold_by("user2").size() == 0);

        ticketDAO.changetickettobought("hgowl45394", "user1");
        ticketDAO.changetickettobought("bdhds83473", "user2");

        assertTrue(ticketDAO.tickets_sold_by("Dream").size() == 3);
        assertTrue(ticketDAO.tickets_sold_by("user1").size() == 0);
        assertTrue(ticketDAO.tickets_sold_by("user2").size() == 0);

        ticketDAO.changetickettobought("hgowl45394", "null");
        assertTrue(ticketDAO.tickets_sold_by("Dream").size() == 2);

        ticketDAO.changetickettobought("bdhds83473", "null");
        assertTrue(ticketDAO.tickets_sold_by("Dream").size() == 1);
    }

    @Test
    public void testGetSpent() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        TicketDAO ticketDAO = dbManager.getTickDAO();

        assertEquals(150.00, ticketDAO.getSpent("GSagh"));
        assertEquals(0.00, ticketDAO.getSpent("nshan"));
        assertEquals(0.00, ticketDAO.getSpent("Dream"));
    }

    @Test
    public void testGetEarned() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        TicketDAO ticketDAO = dbManager.getTickDAO();

        assertEquals(0.00, ticketDAO.getEarned("GSagh"));
        assertEquals(0.00, ticketDAO.getEarned("nshan"));
        assertEquals(150.00, ticketDAO.getEarned("Dream"));
    }
}
