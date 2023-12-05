package BB;

import java.sql.Time;
import java.sql.Date;

public class Ticket{
    private String ticket_id;
    private String event_name;
    private String post_date;
    private String posted_by;
    private String bought_by;
    private String category;
    private double price;
    private String description;
    private String image;
    private String type;
    private String currBuyer;
    private Date auctDate;
    private Time auctTime;


    public Ticket(String ticket_id, String event_name, String date, String posted_by, String bought_by, String category, double price, String description, String image, String type, String currbuyer, Date auctDate, Time auctTime){
        this.ticket_id = ticket_id;
        this.event_name = event_name;
        this.post_date = date;
        this.posted_by = posted_by;
        this.bought_by = bought_by;
        this.category = category;
        this.price = price;
        this.description = description;
        this.image = image;
        this.auctDate= auctDate;
        this.auctTime= auctTime;
        this.type = type;
        this.currBuyer = currbuyer;
    }

    public Ticket(){}

    public String getTicket_id() {
        return ticket_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getPost_date() {
        return post_date;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public String getBought_by() {
        return bought_by;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getType(){return type;}

    public String getCurrBuyer(){return currBuyer;}

    public Date getAuctDate(){return auctDate;}

    public Time getAuctTime(){return auctTime;}

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }

    public void setBought_by(String bought_by) {
        this.bought_by = bought_by;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setType(String type){this.type = type;}

    public void setCurrBuyer(String currBuyer){this.currBuyer =  currBuyer;}

    public void setAuctDate(Date auctDate){this.auctDate =  auctDate;}

    public void setAuctTime(Time auctTime){this.auctTime =  auctTime;}
}

