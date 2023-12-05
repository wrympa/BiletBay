package BB;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.text.*;


public class Event {
    private String event_name;
    private String category;
    private String description;
    private String date;
    private String time;
    private String picture;
    private boolean approved;


    public Event(String event_name, String category, String description, String event_date,String time,String picture, int approvedArg) throws Exception{
        this.event_name = event_name;
        this.category = category;
        this.description = description;
        this.date =event_date;
        this.time = time;
        this.picture = picture;
        if (approvedArg == 1) {
            this.approved = true;
        } else  this.approved = false;
    }

//    public void createEvent(String event_name, String category, String description, Integer popularity, String event_date,String time,String picture) throws Exception{
//        this.event_name = event_name;
//        this.category = category;
//        this.description = description;
//        this.popularity = popularity;
//        this.date =event_date;
//        this.time = time;
//        this.picture = picture;
//    }

    public String getEvent_picture() {
        return this.picture;
    }

    public String getEvent_name() {
        return this.event_name;
    }
    public String getEvent_time() {
        return this.time;
    }
    public String getEvent_category() {
        return this.category;
    }


    public String getEvent_date() throws Exception {
        return this.date;
    }
    public String getEvent_description() { return this.description; }

    public void change_Event_name(String new_name) {
        this.event_name=new_name;
    }

    public void change_Event_category(String new_category) {
        this.category=new_category;
    }

    public void change_Event_description(String new_description) {
        this.description=new_description;
    }

    public void change_Event_date(String new_date) throws Exception{
        this.date=new_date;
    }

    public void change_Event_time(String new_time) {
        this.time = new_time;
    }

    public void change_Event_picture(String new_picture) {
        this.picture = new_picture;
    }

    public String get_Html_Style(){return "<div class=\"event\"><a style=\"text-decoration:none; color:white;\" href=\"event.jsp?id="+event_name +"\" class=\"xshift\">"+ event_name +"</a><img class=\"evimage\" src=\"eventpicks/" +picture+"\"></div><br>";}

    public void setApproved() {
        this.approved = true;
    }
    public boolean getApproved() {
        return this.approved;
    }
}
