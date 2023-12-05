package DAOs;

import BB.Account;
import BB.Event;
import BB.Ticket;
import org.apache.commons.dbcp2.BasicDataSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDAO {
    private final Connection connection;
    public AccountDAO(BasicDataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public ArrayList<Account> getAllAccounts() throws Exception {
        Statement statement = connection.createStatement();
        ResultSet res_set = statement.executeQuery("select * from accounts;\n");
        ArrayList<Account> toret = new ArrayList<>();
        while(res_set.next()){
            Account acc = new Account(res_set.getString(1), res_set.getString(3), res_set.getString(2), res_set.getString(4), res_set.getInt(6));
            acc.setPasswordHash(res_set.getString(4));
            toret.add(acc);
        }
        return toret;
    }
    public void addAccount(String user, String pfp, String number, String pass) throws SQLException {
        Account acc = new Account(user, number, pfp, pass, 0);

        String update = "INSERT INTO accounts VALUES(?,?,?,?,?, ?);";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, user);
        statement1.setString(2, pfp);
        statement1.setString(3, number);
        statement1.setString(4, acc.getPasswordHash());
        statement1.setString(5, "");
        statement1.setInt(6, 0);
        statement1.execute();
        statement1.close();
    }

    public void editAccount(String oldUsername, String user, String pfp, String number, String passHash) throws SQLException {
        String update = "UPDATE accounts SET username = ?, pfp = ?, phone_number = ?, password = ? WHERE username = ?;";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, user);
        statement1.setString(2, pfp);
        statement1.setString(3, number);
        statement1.setString(4, passHash);
        statement1.setString(5, oldUsername);
        statement1.execute();
        statement1.close();
        update = "UPDATE tickets SET posted_by = ? WHERE posted_by = ?;";
        PreparedStatement statement2 = connection.prepareStatement(update);
        statement2.setString(1, user);
        statement2.setString(2, oldUsername);
        statement2.execute();
        statement2.close();
        update = "UPDATE tickets SET bought_by = ? WHERE bought_by = ?;";
        PreparedStatement statement3 = connection.prepareStatement(update);
        statement3.setString(1, user);
        statement3.setString(2, oldUsername);
        statement3.execute();
        statement3.close();
        update = "UPDATE tickets SET bought_by = ? WHERE bought_by = ?;";
        PreparedStatement statement4 = connection.prepareStatement(update);
        statement4.setString(1, user);
        statement4.setString(2, oldUsername);
        statement4.execute();
        statement4.close();
        update = "UPDATE tickets SET pending_buyer = ? WHERE pending_buyer = ?;";
        PreparedStatement statement5 = connection.prepareStatement(update);
        statement5.setString(1, user);
        statement5.setString(2, oldUsername);
        statement5.execute();
        statement5.close();
        update = "UPDATE interests SET username = ? WHERE username = ?;";
        PreparedStatement statement6 = connection.prepareStatement(update);
        statement6.setString(1, user);
        statement6.setString(2, oldUsername);
        statement6.execute();
        statement6.close();
        update = "UPDATE rating SET rated = ? WHERE rated = ?;";
        PreparedStatement statement7 = connection.prepareStatement(update);
        statement7.setString(1, user);
        statement7.setString(2, oldUsername);
        statement7.execute();
        statement7.close();
        update = "UPDATE rating SET rated_by = ? WHERE rated_by = ?;";
        PreparedStatement statement8 = connection.prepareStatement(update);
        statement8.setString(1, user);
        statement8.setString(2, oldUsername);
        statement8.execute();
        statement8.close();
        update = "UPDATE reports SET reported = ? WHERE reported = ?;";
        PreparedStatement statement9 = connection.prepareStatement(update);
        statement9.setString(1, user);
        statement9.setString(2, oldUsername);
        statement9.execute();
        statement9.close();
        update = "UPDATE reports SET reported_by = ? WHERE reported_by = ?;";
        PreparedStatement statement10 = connection.prepareStatement(update);
        statement10.setString(1, user);
        statement10.setString(2, oldUsername);
        statement10.execute();
        statement10.close();
        update = "UPDATE ticket_requests_table SET sent_by = ? WHERE sent_by = ?;";
        PreparedStatement statement11 = connection.prepareStatement(update);
        statement11.setString(1, user);
        statement11.setString(2, oldUsername);
        statement11.execute();
        statement11.close();
        update = "UPDATE ticket_requests_table SET sent_to = ? WHERE sent_to = ?;";
        PreparedStatement statement12 = connection.prepareStatement(update);
        statement12.setString(1, user);
        statement12.setString(2, oldUsername);
        statement12.execute();
        statement12.close();
    }

    public void removeAccount(String user) throws SQLException {
        String update = "Delete from accounts where username = ?";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, user);
        statement1.execute();
        statement1.close();
        String update1 = "Delete from ticket_requests_table where sent_by = ? or sent_to = ?";
        statement1 = connection.prepareStatement(update1);
        statement1.setString(1, user);
        statement1.setString(2, user);
        statement1.execute();
        statement1.close();
        String update2 = "Delete from interests where username = ?";
        statement1 = connection.prepareStatement(update2);
        statement1.setString(1, user);
        statement1.execute();
        statement1.close();
        String update3 = "Delete from rating where rated = ? or rated_by = ?";
        statement1 = connection.prepareStatement(update3);
        statement1.setString(1, user);
        statement1.setString(2, user);
        statement1.execute();
        statement1.close();
        String update4 = "Delete from tickets where posted_by = ? and bought_by = 'null'";
        statement1 = connection.prepareStatement(update4);
        statement1.setString(1, user);
        statement1.execute();
        statement1.close();
    }
    private String generateHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            String res = hexToString(digest);
            return res;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    private static String hexToString(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (int i=0; i<bytes.length; i++) {
            int val = bytes[i];
            val = val & 0xff;  // remove higher bits, sign
            if (val<16) buff.append('0'); // leading 0
            buff.append(Integer.toString(val, 16));
        }
        return buff.toString();
    }

    public Account tryLogin(String username, String password) throws Exception {
        String passHash = generateHash(password);
        String update = "select * from accounts where username =? and password =?";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, username);
        statement1.setString(2, passHash);
        statement1.execute();
        ResultSet res_set = statement1.getResultSet();
        while(res_set.next()){
            Account acc = new Account(res_set.getString(1), res_set.getString(3), res_set.getString(2), password, res_set.getInt(6));
            return acc;
        }
        return null;
    }

    public boolean contains(String username) throws SQLException {
        String update = "select * from accounts where username = ?";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, username);
        statement1.execute();
        ResultSet res_set = statement1.getResultSet();
        int i = 0;
        while(res_set.next()){
          i++;
        }
        return (i == 1);
    }

    public Account getUserByName(String username) throws Exception {
        String update = "select * from accounts where username = ?";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, username);
        statement1.execute();
        ResultSet res_set = statement1.getResultSet();
        while (res_set.next()) {
            Account acc = new Account(res_set.getString(1), res_set.getString(3), res_set.getString(2), res_set.getString(4), res_set.getInt(6));
            acc.setPasswordHash(res_set.getString(4));
            return acc;
        }
        return null;
    }

    public void makeModerator(String username) throws SQLException {
        String update = "UPDATE accounts SET moderator = 1 WHERE username = ?;";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, username);
        statement1.execute();
        statement1.close();
    }

    public ArrayList<Account> getSearchAccounts(String search) throws Exception {
        search = search.toLowerCase();
        String update = "select * from accounts where instr(lower(username), ?) > 0;";
        PreparedStatement statement1 = connection.prepareStatement(update);
        statement1.setString(1, search);
        statement1.execute();
        ResultSet res_set = statement1.getResultSet();
        ArrayList<Account> found = new ArrayList<>();
        while (res_set.next()) {
            Account acc = new Account(res_set.getString(1), res_set.getString(3), res_set.getString(2), res_set.getString(4), res_set.getInt(6));
            acc.setPasswordHash(res_set.getString(4));
            String interests = res_set.getString(5);
            String curstr = "";
            found.add(acc);
        }
        return found;
    }

    public String getAchievementsAsHtml(Account acc, TicketDAO tickdao) throws Exception {
        String ret = "";
        int bought = tickdao.tickets_bought_by(acc.getUsername()).size();
        if(bought > 10){
            ret += "<div class\"achievement\"><img class=\"achimg\" src=\"achievements/bought10.png\"><div class=\"achdesc\">Ticket Collector:<br>This user has bought more than 10 tickets</div></div>";
        } else if(bought > 5){
            ret += "<div class\"achievement\"><img class=\"achimg\" src=\"achievements/bought5.png\"><div class=\"achdesc\">Frequent Attendee:<br>This user has bought more than 5 tickets</div></div>";
        } else if(bought >= 1){
            ret += "<div class\"achievement\"><img class=\"achimg\" src=\"achievements/bought1.png\"><div class=\"achdesc\">Fan Purchase:<br>This user has bought their first ticket</div></div>";
        }
        int sold = tickdao.tickets_sold_by(acc.getUsername()).size();
        if(sold >= 10){
            ret += "<div class\"achievement\"><img class=\"achimg\" src=\"achievements/sold10.png\"><div class=\"achdesc\">Ticket Dynamo:<br>This user has sold more than 10 tickets</div></div>";
        } else if(sold >= 5){
            ret += "<div class\"achievement\"><img class=\"achimg\" src=\"achievements/sold5.png\"><div class=\"achdesc\">Ticket Peddler:<br>This user has sold more than 5 tickets</div></div>";
        } else if(sold >= 1){
            ret += "<div class\"achievement\"><img class=\"achimg\" src=\"achievements/sold1.png\"><div class=\"achdesc\">First Seller's Step:<br>This user has sold their first ticket</div></div>";
        }
        ret+="\n";
        if(tickdao.getEarned(acc.getUsername()) > 1000){
            ret += "<div class\"achievement\"><img class=\"achimg\" src=\"achievements/made1000.png\"><div class=\"achdesc\">Ticket Tycoon:<br>This user has made more than 1000 dollars selling tickets</div></div>";
        }
        ret+="\n";
        if(tickdao.getSpent(acc.getUsername()) > 1000){
            ret += "<div class\"achievement\"><img class=\"achimg\" src=\"achievements/spent1000.png\"><div class=\"achdesc\">Grand Event Patron:<br>This user has spent more than 1000 dollars buying tickets</div></div>";
        }
        return ret;
    }

    public double getRating(String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select avg(rating) from rating where rated = ?");
        statement.setString(1, username);
        statement.execute();
        double answ = 0;
        ResultSet res_set = statement.getResultSet();
        while (res_set.next()){
            answ = res_set.getDouble(1);
        }
        return answ;
    }
    public void AddRating(String rater, String rated, int rating) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into rating values(?, ?, ?)");
        statement.setString(1, rated);
        statement.setString(2, rater);
        statement.setInt(3, rating);
        statement.execute();
    }

    public boolean RatedAlready(String rater, String rated) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select count(*) from rating where rated = ? and rated_by = ?");
        statement.setString(1, rated);
        statement.setString(2, rater);
        statement.execute();
        ResultSet res_set = statement.getResultSet();
        while (res_set.next()){
            if(res_set.getDouble(1) > 0) return true;
        }
        return false;
    }
    public void UpdateRating(String rater, String rated, int rating) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("update rating set rating = ? where rated = ? and rated_by = ?");
        statement.setInt(1, rating);
        statement.setString(2, rated);
        statement.setString(3, rater);
        statement.execute();
    }
}
