package DAOs;

import BB.Event;
import BB.Report;
import BB.SQLTablesManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.testng.AssertJUnit.*;

public class TestReportDAO {
    @Test
    public void basicTest() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        ReportDAO reportDAO = dbManager.getReportDAO();

        ArrayList<Report> reps=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            reps.add(new Report("snitchnumber"+i, "bossnumber"+i, "ranaracketonstreet"+i));
            reportDAO.AddReport("bossnumber"+i, "snitchnumber"+i,"ranaracketonstreet"+i);
        }
        ArrayList<Report> tocomp = reportDAO.GetAllReports();
        for (int i = 0; i < 20; i++) {
            assertEquals(reps.get(i).getReported(), tocomp.get(i).getReported());
            assertEquals(reps.get(i).getReporter(), tocomp.get(i).getReporter());
            assertEquals(reps.get(i).getReason(), tocomp.get(i).getReason());
        }
        for (int i = 0; i < 20; i++) {
            if(i%2==0){
                reportDAO.RemoveReportsOf("bossnumber"+i);
            }else{
                reportDAO.RemoveReportsBy("snitchnumber"+i);
            }
        }
    }
    @Test
    public void advancedTest() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        ReportDAO reportDAO = dbManager.getReportDAO();

        for (int i = 0; i < 20; i++) {
            reportDAO.AddReport("bossnumber"+(i%10), "snitchnumber"+(i%3),"ranaracketonstreet"+i);
        }
        assertEquals(20, reportDAO.GetAllReports().size());
        reportDAO.RemoveReportsBy("snitchnumber0");
        assertEquals(13, reportDAO.GetAllReports().size());
        reportDAO.RemoveReportsOf("bossnumber1");
        assertEquals(11, reportDAO.GetAllReports().size());
        for (int i = 0; i < 20; i++) {
            if(i%2==0){
                reportDAO.RemoveReportsOf("bossnumber"+i);
            }else{
                reportDAO.RemoveReportsBy("snitchnumber"+i);
            }
        }
    }
}
