package DAOs;

import BB.Account;
import BB.Event;
import BB.Request;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {
    private static Connection connection;
    private final TicketDAO ticketDAO;
    public RequestDAO(BasicDataSource dataSource, TicketDAO ticketDAO) throws SQLException {
        this.connection = dataSource.getConnection();
        this.ticketDAO = ticketDAO;
    }

    public ArrayList<Request> getAllAccounts() throws Exception {
        Statement statement = connection.createStatement();
        ResultSet res_set = statement.executeQuery("select * from ticket_requests_table;\n");
        ArrayList<Request> result = new ArrayList<>();
        while (res_set.next()) {
            Request req = new Request(res_set.getString(1), res_set.getString(2), res_set.getString(3));
            result.add(req);
        }
        return result;
    }

    public void sendRequest(String sentBy, String sentTo, String ticketId) throws SQLException {
        String update = "INSERT INTO ticket_requests_table VALUES(?,?,?);";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, sentBy);
        statement1.setString(2, sentTo);
        statement1.setString(3, ticketId);
        statement1.execute();
        statement1.close();
    }
    public void rejectRequest(String sentBy, String sentTo, String ticketId) throws SQLException {
        String update = "Delete from ticket_requests_table where sent_by = ? and sent_to = ? and ticket_id = ?;";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, sentBy);
        statement1.setString(2, sentTo);
        statement1.setString(3, ticketId);
        statement1.execute();
        statement1.close();
    }

    public void deleteRequests(String ticketId) throws SQLException {
        String update = "Delete from ticket_requests_table where ticket_id = ?;";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, ticketId);
        statement1.execute();
        statement1.close();
    }


    public boolean containsRequest(String sentBy, String sentTo, String ticketId) throws SQLException {
        String update = "select * from ticket_requests_table where sent_by = ? and sent_to = ? and ticket_id = ?;";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, sentBy);
        statement1.setString(2, sentTo);
        statement1.setString(3, ticketId);
        statement1.execute();
        ResultSet res_set = statement1.getResultSet();
        int count = 0;
        while (res_set.next()) count++;

        return count > 0;
    }

    public List<Request> getRequests(String sentTo) throws Exception {
        String update = "select * from ticket_requests_table where sent_to = ?;";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, sentTo);
        statement1.execute();
        ResultSet res_set = statement1.getResultSet();
        List<Request> temp = new ArrayList<>();

        while (res_set.next()) {
            Request request = new Request(res_set.getString(1), res_set.getString(2), res_set.getString(3));
            temp.add(request);
        }
        return temp;
    }
}
