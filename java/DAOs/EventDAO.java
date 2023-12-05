package DAOs;

import BB.Event;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventDAO {
    private final Connection connection;
    public EventDAO(BasicDataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public ArrayList<Event> GetAllEvents() throws Exception {
        String s = "select * from events where approved = ?";
        PreparedStatement statement = connection.prepareStatement(s);
        statement.setInt(1, 1);
        ResultSet res_set = statement.executeQuery();
        ArrayList<Event> toret = new ArrayList<>();
        while(res_set.next()){
            Event ev = new Event(res_set.getString(1), res_set.getString(5), res_set.getString(7), res_set.getString(3), res_set.getString(4), res_set.getString(6), res_set.getInt(8));
            toret.add(ev);
        }
        return toret;
    }
    public Event GetEventByName(String eventname) throws Exception {
//        Statement statement = connection.createPreparedStatement();
        PreparedStatement prepstat = connection.prepareStatement("select * from events where event_name = ?");
//        ResultSet res_set = statement.executeQuery("select * from events where event_name = "+eventname+";\n");
        prepstat.setString(1, eventname);
        prepstat.execute();
        ResultSet res_set = prepstat.getResultSet();
        while(res_set.next()){
            Event ev = new Event(res_set.getString(1), res_set.getString(5), res_set.getString(7), res_set.getString(3), res_set.getString(4), res_set.getString(6), res_set.getInt(8));
            return ev;
        }
        return null;
    }

    public void addevent(String name, int popularity, String date, String time, String category, String img, String desc, int approved) throws Exception {
        String update = "INSERT INTO events VALUES(?,?,?,?,?,?,?,?);";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, name);
        statement1.setInt(2, popularity);
        statement1.setString(3, date);
        statement1.setString(4, time);
        statement1.setString(5, category);
        statement1.setString(6, img);
        statement1.setString(7, desc);
        statement1.setInt(8, approved);
        statement1.execute();
        statement1.close();
    }
    public void removeevent(String name) throws SQLException {
        String update3 = "delete from ticket_requests_table r where (select t.event_name from tickets t where t.ticket_id = r.ticket_id) = ?";
        PreparedStatement statement1 = connection.prepareStatement(update3);
        statement1.setString(1, name);
        statement1.execute();
        statement1.close();
        String update = "Delete from events where event_name = ?";
        statement1 = connection.prepareStatement(update);
        statement1.setString(1, name);
        statement1.execute();
        statement1.close();
        String update1 = "Delete from tickets where event_name = ?";
        statement1 = connection.prepareStatement(update1);
        statement1.setString(1, name);
        statement1.execute();
        statement1.close();
        String update2 = "delete from interests where event_name=?";
        statement1 = connection.prepareStatement(update2);
        statement1.setString(1, name);
        statement1.execute();
        statement1.close();

    }

    public ArrayList<Event> gettop3() throws Exception {
        Statement statement = connection.createStatement();
        ResultSet res_set = statement.executeQuery("select * from events e order by (select count(*) from interests i where i.event_name = e.event_name) desc");
        ArrayList<Event> temp = new ArrayList<>();
        int i = 3;
        while(res_set.next()){
            if(i == 0) break;
            Event ev = new Event(res_set.getString(1), res_set.getString(5), res_set.getString(7), res_set.getString(3), res_set.getString(4), res_set.getString(6), res_set.getInt(8));
            temp.add(ev);
            i--;
        }
        return temp;
    }

    public List<Event> events_of_category(String category, String sortBy, String order) throws Exception {
        String update = "select * from events e where e.category = ? and e.approved = ?";
        if (!sortBy.equals("")) {
            if(sortBy.equals("popularity")){
                update += " order by (select count(*) from interests i where i.event_name = e.event_name)";
            } else update += " order by " + sortBy;
            if (order.equals("descending")) update += " desc;";
        }
        PreparedStatement statement = connection.prepareStatement(update);
        statement.setString(1, category);
        statement.setInt(2, 1);
        statement.execute();
        ResultSet res_set = statement.getResultSet();
        ArrayList<Event> temp = new ArrayList<>();
        while(res_set.next()){
            Event ev = new Event(res_set.getString(1), res_set.getString(5), res_set.getString(7), res_set.getString(3), res_set.getString(4), res_set.getString(6), res_set.getInt(8));
            temp.add(ev);
        }
        return temp;
    }

    public List<Event> search_event(String event, String sortBy, String order) throws Exception {
        event = event.toLowerCase();
        PreparedStatement prepstat;
        String update = "select * from events e where instr(lower(e.event_name), ?) > 0 and e.approved = ?";
        if (!sortBy.equals("")) {
            if(sortBy.equals("popularity")){
                update += " order by (select count(*) from interests i where i.event_name = e.event_name)";
            } else update += " order by " + sortBy;
            if (order.equals("descending")) update += " desc;";
        }
        prepstat = connection.prepareStatement(update);
        prepstat.setString(1, event);
        prepstat.setInt(2, 1);
        prepstat.execute();
        ResultSet res_set = prepstat.getResultSet();


        ArrayList<Event> temp = new ArrayList<>();
        while(res_set.next()){
            Event ev = new Event(res_set.getString(1), res_set.getString(5), res_set.getString(7), res_set.getString(3), res_set.getString(4), res_set.getString(6), res_set.getInt(8));
            temp.add(ev);
        }
        return temp;
    }

    public ArrayList<Event> getUnapprovedEvents() throws Exception {
        String s = "select * from events where approved = ?";
        PreparedStatement statement = connection.prepareStatement(s);
        statement.setInt(1, 0);
        ResultSet res_set = statement.executeQuery();
        ArrayList<Event> temp = new ArrayList<>();
        while(res_set.next()){
            Event ev = new Event(res_set.getString(1), res_set.getString(5), res_set.getString(7), res_set.getString(3), res_set.getString(4), res_set.getString(6), res_set.getInt(8));
            temp.add(ev);
        }
        return temp;
    }

    public void approveEvent(String eventName) throws SQLException {
        String update = "UPDATE events SET approved = 1 WHERE event_name = ?;";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, eventName);
        statement1.execute();
        statement1.close();
    }

    public List<Event> getSortedEvents(String sortBy, String order) throws Exception {
        String update = "select * from events where approved = ? order by " + sortBy;
        if(sortBy.equals("popularity")){
            update = "select * from events e where approved = ? order by (select count(*) from interests i where i.event_name = e.event_name)";
        }
        if (order.equals("descending")) update += " desc;";
        PreparedStatement statement = connection.prepareStatement(update);
        statement.setInt(1, 1);
        ResultSet res_set = statement.executeQuery();
        ArrayList<Event> toret = new ArrayList<>();
        while (res_set.next()){
            Event ev = new Event(res_set.getString(1), res_set.getString(5), res_set.getString(7), res_set.getString(3), res_set.getString(4), res_set.getString(6), res_set.getInt(8));
            toret.add(ev);
        }
        return toret;
    }

    public int getEvent_popularity(String eventName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select count(*) from interests where event_name = ?");
        statement.setString(1, eventName);
        statement.execute();
        ResultSet res_set = statement.getResultSet();
        int answ = 0;
        while (res_set.next()){
            answ = res_set.getInt(1);
        }
        return answ;
    }
}
