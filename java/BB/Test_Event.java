package BB;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.AssertFalse;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class Test_Event {
    @Test
    public void testEvent() throws Exception {
        String name1 = "Imagine Dragons";
        String category1 = "music";
        String descr1 = "too cool";
        String date1 = "01/09/2023";
        String time1 = "15:30";
        String picture1 = "picpic1";
        Integer popularity1 = 1001;
        Event event1 = new Event(name1,category1,descr1,date1,time1,picture1, 1);
        assertEquals(name1,event1.getEvent_name());
        assertEquals(category1,event1.getEvent_category());
        assertEquals(descr1,event1.getEvent_description());
        assertEquals(time1,event1.getEvent_time());
        assertEquals(date1, event1.getEvent_date());
        assertEquals(picture1,event1.getEvent_picture());

        String name2 = "uefa u21";
        String category2 = "cinema";
        String descr2 = "rame teqsti";
        String date2 = "12/03/2024";
        String time2 = "23:00";
        String picture2 = "picpic2";
        Integer popularity2 = 12345;
        Event event2 = new Event(name2,category2,descr2,date2,time2,picture2,1);

        assertEquals(name2,event2.getEvent_name());
        assertEquals(category2,event2.getEvent_category());
        assertEquals(descr2,event2.getEvent_description());
        assertEquals(time2,event2.getEvent_time());
        assertEquals(date2, event2.getEvent_date());
        assertEquals(picture2,event2.getEvent_picture());
    }

    @Test
    public void testAll() throws Exception {
        String name1 = "Imagine Dragons";
        String category1 = "music";
        String descr1 = "too cool";
        String date1 = "01/09/2023";
        String time1 = "15:30";
        Integer popularity1 = 1001;
        String picture1 = "picpic1";
        Event event1 = new Event(name1,category1,descr1,date1,time1,picture1, 0);
        assertEquals(event1.get_Html_Style(),"<div class=\"event\"><a style=\"text-decoration:none; color:white;\" href=\"event.jsp?id="+name1 +"\" class=\"xshift\">"+ name1 +"</a><img class=\"evimage\" src=\"eventpicks/" +picture1+"\"></div><br>");
        assertFalse(event1.getApproved());
        String name2 = "uefa u21";
        String category2 = "sport";
        String descr2 = "rame teqsti";
        String date2 = "12/03/2024";
        String time2 = "23:00";
        String picture2 = "picpic2";
        Integer popularity2 = 12345;
        event1.change_Event_name(name2);
        event1.change_Event_category(category2);
        event1.change_Event_description(descr2);
        event1.change_Event_date(date2);
        event1.change_Event_time(time2);
        event1.change_Event_picture(picture2);
        event1.setApproved();
        assertTrue(event1.getApproved());
        assertEquals(name2,event1.getEvent_name());
        assertEquals(category2,event1.getEvent_category());
        assertEquals(descr2,event1.getEvent_description());
        assertEquals(time2,event1.getEvent_time());
        assertEquals(date2, event1.getEvent_date());
        assertEquals(picture2,event1.getEvent_picture());
    }



}