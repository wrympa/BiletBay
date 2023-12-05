package BB;

import org.testng.annotations.Test;

import java.sql.Time;
import java.util.Calendar;
import java.sql.Date;

import static org.testng.AssertJUnit.*;

public class Test_Ticket {
    @Test
    public void test1(){
        Ticket t = new Ticket();
        t.setTicket_id("1234567890");
        t.setEvent_name("Barbie");
        t.setPost_date("07-20-2023");
        t.setPosted_by("user1");
        t.setBought_by("null");
        t.setCategory("1");
        t.setPrice(25.50);
        t.setDescription("Let's go party");
        t.setImage("1234567890.jpg");
        t.setType("auc");
        t.setCurrBuyer("null");
        t.setAuctDate(new Date(2023, Calendar.JULY,20));
        t.setAuctTime(new Time(15, 0, 0));
        assertEquals("1234567890", t.getTicket_id());
        assertEquals("Barbie", t.getEvent_name());
        assertEquals("07-20-2023", t.getPost_date());
        assertEquals("user1", t.getPosted_by());
        assertEquals("null", t.getBought_by());
        assertEquals("1", t.getCategory());
        assertEquals(25.50, t.getPrice());
        assertEquals("Let's go party", t.getDescription());
        assertEquals("1234567890.jpg", t.getImage());
        assertEquals("auc", t.getType());
        assertEquals("null", t.getCurrBuyer());
        assertEquals(new Date(2023,Calendar.JULY,20), t.getAuctDate());
        assertEquals(new Time(15, 0, 0), t.getAuctTime());
    }

    @Test
    public void test2(){
        Ticket t = new Ticket("9876543210",
                                "Oppenheimer",
                                "07-28-2023",
                                "user2",
                                "user1",
                                "2",
                                30,
                                "enjoy",
                                "9876543210.jpg",
                                "buy",
                                "user1",
                                null,
                                null);


        assertEquals("9876543210", t.getTicket_id());
        assertEquals("Oppenheimer", t.getEvent_name());
        assertEquals("07-28-2023", t.getPost_date());
        assertEquals("user2", t.getPosted_by());
        assertEquals("user1", t.getBought_by());
        assertEquals("2", t.getCategory());
        assertEquals(30.00, t.getPrice());
        assertEquals("enjoy", t.getDescription());
        assertEquals("9876543210.jpg", t.getImage());
        assertEquals("buy", t.getType());
        assertEquals("user1", t.getCurrBuyer());
        assertNull(t.getAuctDate());
        assertNull(t.getAuctTime());


        t.setTicket_id("2345678901");
        assertFalse("9876543210".equals(t.getTicket_id()));
        assertTrue("2345678901".equals(t.getTicket_id()));

        t.setEvent_name("Imagine Dragons");
        assertFalse("Oppenheimer".equals(t.getEvent_name()));
        assertTrue("Imagine Dragons".equals(t.getEvent_name()));

        t.setPost_date("07-30-2023");
        assertFalse("07-28-2023".equals(t.getPost_date()));
        assertTrue("07-30-2023".equals(t.getPost_date()));

        t.setPosted_by("user3");
        assertFalse("user2".equals(t.getPosted_by()));
        assertTrue("user3".equals(t.getPosted_by()));

        t.setBought_by("null");
        assertFalse("user1".equals(t.getBought_by()));
        assertTrue("null".equals(t.getBought_by()));

        t.setCategory("0");
        assertFalse("2".equals(t.getCategory()));
        assertTrue("0".equals(t.getCategory()));

        t.setPrice(20);
        assertFalse(30.00 == t.getPrice());
        assertTrue(20 == t.getPrice());

        t.setDescription("Ticket Changed");
        assertFalse("enjoy".equals(t.getDescription()));
        assertTrue("Ticket Changed".equals(t.getDescription()));

        t.setImage("2345678901.jpg");
        assertFalse("9876543210.jpg".equals(t.getImage()));
        assertTrue("2345678901.jpg".equals(t.getImage()));

        t.setType("auc");
        assertFalse("buy".equals(t.getType()));
        assertTrue("auc".equals(t.getType()));

        t.setCurrBuyer("user2");
        assertFalse("user1".equals(t.getCurrBuyer()));
        assertTrue("user2".equals(t.getCurrBuyer()));

        t.setAuctDate(new Date(2023,Calendar.JULY,20));
        assertNotNull(t.getAuctDate());
        assertTrue(new Date(2023, Calendar.JULY,20).equals(t.getAuctDate()));

        t.setAuctTime(new Time(15, 0, 0));
        assertNotNull(t.getAuctTime());
        assertTrue(new Time(15, 0, 0).equals(t.getAuctTime()));
    }
}
