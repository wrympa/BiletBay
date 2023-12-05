package BB;

import java.sql.*;

import java.sql.PreparedStatement;
import java.util.concurrent.TimeUnit;

public class AuctionChecker extends Thread{
    private Connection connection = null;
    public AuctionChecker(Connection connection){
        this.connection = connection;
    }

    public void run(){
        while (true){
            String update = "update tickets set bought_by = pending_buyer where type = 'auc' and pending_buyer <> 'null' and ((CURRENT_TIMESTAMP = auct_date and CAST(current_timestamp AS TIME(0)) > auct_time) or CURRENT_TIMESTAMP > auct_date);";
            PreparedStatement statement1 = null;
            try {
                statement1 = connection.prepareStatement(update);
                statement1.executeUpdate();
                statement1.close();
                TimeUnit.MINUTES.sleep(5);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



    }
}
