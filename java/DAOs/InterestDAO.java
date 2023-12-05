package DAOs;

import BB.Account;
import BB.Event;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class InterestDAO {

    private Connection connection;
    private EventDAO eventDAO;

    public InterestDAO(BasicDataSource stream, EventDAO eventDAO) throws SQLException {
        this.connection = stream.getConnection();
        this.eventDAO = eventDAO;
    }

    public boolean isinterested(String username, String eventname) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select count(*) from interests where event_name = ? and username = ?");
        statement.setString(1, eventname);
        statement.setString(2, username);
        statement.execute();
        ResultSet res_set = statement.getResultSet();
        int a = 0;
        while (res_set.next()){
            a = res_set.getInt(1);
        }
        return a > 0;
    }

    public void AddInterest(Account acc, String eventname) throws Exception {
        PreparedStatement statement = connection.prepareStatement("insert into interests values(?, ?)");
        statement.setString(1, eventname);
        statement.setString(2, acc.getUsername());
        statement.execute();
    }
    public void RemoveInterest(Account acc, String eventname) throws Exception {
        PreparedStatement statement = connection.prepareStatement("delete from interests where event_name = ? and username = ?");
        statement.setString(1, eventname);
        statement.setString(2, acc.getUsername());
        statement.execute();
    }

    public ArrayList<Event> getInterestedEvents(String username) throws Exception {
        PreparedStatement statement = connection.prepareStatement("select * from interests where username = ?");
        statement.setString(1, username);
        statement.execute();
        ResultSet res_set = statement.getResultSet();
        ArrayList<Event> toret = new ArrayList<>();
        while(res_set.next()){
            toret.add(eventDAO.GetEventByName(res_set.getString(1)));
        }
        return toret;
    }

}
