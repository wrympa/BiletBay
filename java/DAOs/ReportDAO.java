package DAOs;

import BB.Account;
import BB.Report;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    private final Connection connection;
    public ReportDAO(BasicDataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public ArrayList<Report> GetAllReports() throws SQLException {
        ArrayList<Report> toret = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("select * from reports");
        statement.execute();
        ResultSet res_set = statement.getResultSet();
        while (res_set.next()){
            Report re = new Report(res_set.getString(2), res_set.getString(1), res_set.getString(3));
            toret.add(re);
        }
        return toret;
    }

    public void AddReport(String reported, String reporter, String reason) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into reports values(?,?,?)");
        statement.setString(1, reported);
        statement.setString(2, reporter);
        statement.setString(3, reason);
        statement.execute();
    }
    public void RemoveReportsBy(String reporter) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("delete from reports where reported_by=?");
        statement.setString(1, reporter);
        statement.execute();
    }
    public void RemoveReportsOf(String reported) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("delete from reports where reported=?");
        statement.setString(1, reported);
        statement.execute();
    }
    public void RemoveReport(String reported, String reporter) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("delete from reports where reported=? and reported_by=?");
        statement.setString(1, reported);
        statement.setString(2, reporter);
        statement.execute();
    }
}
