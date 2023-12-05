package BB;

import DAOs.*;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;


public class SQLTablesManager {
    private AccountDAO AccDAO;
    private TicketDAO TickDAO;
    private EventDAO EventDAO;
    private RequestDAO requestDAO;
    private ReportDAO reportDAO;
    private InterestDAO interestDAO;
    private BasicDataSource stream;

    public AccountDAO getAccDAO(){return AccDAO;}
    public TicketDAO getTickDAO(){return TickDAO;}
    public EventDAO getEventDAO(){return EventDAO;}
    public RequestDAO getRequestDAO() {return requestDAO;}
    public ReportDAO getReportDAO() {return reportDAO;}
    public InterestDAO getInterestDAO() {return interestDAO;}
    public SQLTablesManager(String database) throws Exception {
        this.stream = new BasicDataSource();
        stream.setUrl("jdbc:mysql://localhost:3306/" + database);
        stream.setUsername("root");
        stream.setPassword("Dati2003619"); //aq tqveni paroli
        EventDAO = new EventDAO(stream);
        TickDAO = new TicketDAO(stream);
        AccDAO = new AccountDAO(stream);
        interestDAO = new InterestDAO(stream, EventDAO);
        requestDAO = new RequestDAO(stream, TickDAO);
        reportDAO = new ReportDAO(stream);
    }

    public Connection getconnection() throws SQLException {
        return stream.getConnection();
    }
}
