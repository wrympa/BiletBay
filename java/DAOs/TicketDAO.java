package DAOs;

import BB.Request;
import BB.Ticket;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    private static Connection connection = null;
    public TicketDAO(BasicDataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public List<Ticket> filter_sort(String event_name, double min, double max, int sort, String type) throws SQLException {
        String select = "select * from tickets ";

        String where = "where event_name = ?";

        where += " and price between ? and ?";

        if(!type.isEmpty()){
            where += " and type = ?";
        }

        String order = "";
        if(sort == 1){
            order += " order by price";
        }else if(sort == 2){
            order += " order by price desc";
        }

        String s = select + where + order;

        PreparedStatement statement = connection.prepareStatement(s);
        statement.setString(1, event_name);
        statement.setDouble(2, min);
        statement.setDouble(3, max);
        if(!type.isEmpty()){
            statement.setString(4, type);
        }

        List<Ticket> tickets = new ArrayList<>();

        ResultSet result = statement.executeQuery();
        while (result.next()) {
            tickets.add(resultToTicket(result));
        }
        statement.close();

        return tickets;
    }

    public List<Ticket> getAllByEventName(String event_name) throws SQLException {
       String s = "select * from tickets" +
                  " where bought_by = 'null' and event_name = ?";
        return executeStatement(s, event_name);
    }

    public List<Ticket> tickets_bought_by(String user) throws SQLException {
        String s = "select * from tickets where bought_by = ?";
        return executeStatement(s, user);
    }

    public List<Ticket> tickets_posted_by(String user) throws SQLException {
        String s = "select * from tickets where posted_by = ? and bought_by = 'null'";
        return executeStatement(s, user);
    }
    public List<Ticket> tickets_sold_by(String user) throws SQLException {
        String s = "select * from tickets where posted_by = ? and bought_by <> 'null'";
        return executeStatement(s, user);
    }

    public boolean exists(String ticket_id) throws SQLException {
        String s = "select * from tickets where ticket_id = ?";
        List<Ticket> l =  executeStatement(s, ticket_id);
        return l.size() != 0;
    }

    public boolean userOwnsTicket(String user, String ticketId) throws SQLException {
        String str = "select * from tickets where posted_by = ? and ticket_id = ?;";
        PreparedStatement st = connection.prepareStatement(str);
        st.setString(1, user);
        st.setString(2, ticketId);
        st.execute();
        ResultSet res_set = st.getResultSet();
        int count = 0;
        while (res_set.next()) count++;
        return count == 1;
    }

    public void edit(String id, String event, String category, double price, String description,
                     String type, Date auctDate, Time auctTime) throws SQLException {
        String update = "update tickets set event_name = ?, category = ?, price = ?, description = ?," +
                " type = ?, auct_date = ?, auct_time = ? where ticket_id = ?;";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, event);
        statement1.setString(2, category);
        statement1.setDouble(3, price);
        statement1.setString(4, description);
        statement1.setString(5, type);
        statement1.setDate(6, auctDate);
        statement1.setTime(7, auctTime);
        statement1.setString(8, id);
        statement1.execute();
        statement1.close();
    }

    private static List<Ticket> executeStatement(String s, String setString) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(s);
        statement.setString(1, setString);
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            tickets.add(resultToTicket(result));
        }
        statement.close();
        return tickets;
    }

    private static Ticket resultToTicket(ResultSet result) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setTicket_id(result.getString("ticket_id"));
        ticket.setEvent_name(result.getString("event_name"));
        ticket.setPost_date(result.getString("post_date"));
        ticket.setPosted_by(result.getString("posted_by"));
        ticket.setBought_by(result.getString("bought_by"));
        ticket.setCategory(result.getString("category"));
        ticket.setPrice(Double.parseDouble(result.getString("price")));
        ticket.setDescription(result.getString("description"));
        ticket.setImage(result.getString("img"));
        ticket.setType(result.getString("type"));
        ticket.setAuctDate(result.getDate("auct_date"));
        ticket.setAuctTime(result.getTime("auct_time"));
        ticket.setCurrBuyer(result.getString("pending_buyer"));
        return ticket;
    }

    public static void removeticket(String id) throws SQLException {
        String update = "Delete from tickets where ticket_id = ?";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, id);
        statement1.execute();
        statement1.close();
        String update1 = "Delete from ticket_requests_table where ticket_id = ?";
        statement1 = connection.prepareStatement(update1);
        statement1.setString(1, id);
        statement1.execute();
        statement1.close();
    }

    public void addticket(String t_id, String e_name, String date, String post_id, String buy_id, String category, double price, String descrp, String img, String type, String currBuyer, Date auctDate, Time auctTime) throws SQLException {
        String update = "INSERT INTO tickets VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, t_id);
        statement1.setString(2, e_name);
        statement1.setDate(3, Date.valueOf(date));
        statement1.setString(4, post_id);
        statement1.setString(5, buy_id);
        statement1.setString(6, category);
        statement1.setDouble(7, price);
        statement1.setString(8, descrp);
        statement1.setString(9, img);
        statement1.setString(10, type);
        statement1.setString(11, currBuyer);
        statement1.setDate(12, auctDate);
        statement1.setTime(13, auctTime);
        statement1.execute();
        statement1.close();
    }

    public void changetickettobought(String t_id, String buy_id) throws SQLException {
        String update = "update tickets set bought_by = ? where ticket_id = ?;";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, buy_id);
        statement1.setString(2, t_id);
        statement1.executeUpdate();
        statement1.close();
    }

    public void placebid(String t_id, String buy_id, float price) throws SQLException {
        String update = "update tickets set pending_buyer = ?, price = ? where ticket_id = ? and ((cast(CURRENT_TIMESTAMP as date) = auct_date and CAST(current_timestamp AS TIME(0)) < auct_time) or cast(CURRENT_TIMESTAMP as date) < auct_date);";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, buy_id);
        statement1.setFloat(2, price);
        statement1.setString(3, t_id);
        statement1.executeUpdate();
        statement1.close();
    }

    public Ticket getticketbyname(String ticket_id) throws SQLException {
        String s = "select * from tickets where ticket_id = ?";
        List<Ticket> l =  executeStatement(s, ticket_id);
        return l.get(0);
    }

    public int getsoldcount() throws SQLException {
        String update = "select * from tickets where bought_by <> ? and posted_by <> ?";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, "null");
        statement1.setString(2, "null");
        statement1.execute();
        ResultSet res_set = statement1.getResultSet();
        int count = 0;
        while(res_set.next()){
            count++;
        }
        return count;
    }

    public int getStatistics(String event, String name) throws SQLException {
        String update = "select * from tickets where true";
        int how = 0;
        if(!event.equals("")){
            update += " and event_name = ? and bought_by <> 'null'";
            how = 1;
        }
        if(!name.equals("")){
            update += " and bought_by = ?";
            if(how == 1) how = 2;
            else how = 3;
        }
        PreparedStatement statement1 = connection.prepareStatement(update);

        if (how == 1){
            statement1.setString(1, event);
        }
        if (how == 2){
            statement1.setString(1, event);
            statement1.setString(2, name);
        }
        if (how == 3){
            statement1.setString(1, name);
        }
        statement1.execute();
        ResultSet res_set = statement1.getResultSet();
        int count = 0;
        while(res_set.next()){
            count++;
        }
        return count;
    }

    public double getSpent(String user) throws Exception {
        double res = 0;
        String update = "select sum(price) from tickets where bought_by = ?;";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, user);
        statement1.execute();
        ResultSet res_set = statement1.getResultSet();
        while (res_set.next()) {
            res += res_set.getDouble(1);
        }
        return res;
    }

    public double getEarned(String user) throws Exception {
        double res = 0;
        String update = "select sum(price) from tickets where posted_by = ? and bought_by <> ?;";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, user);
        statement1.setString(2, "null");
        statement1.execute();
        ResultSet res_set = statement1.getResultSet();
        while (res_set.next()) {
            res += res_set.getDouble(1);
        }
        return res;
    }
}
