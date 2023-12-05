package DAOs;

import BB.Account;
import BB.Event;
import BB.SQLTablesManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.testng.AssertJUnit.*;

public class TestAccountDAO {

    @Test
    public void testGetSearchAccounts() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        AccountDAO accountDAO = dbManager.getAccDAO();

        assertTrue(accountDAO.getSearchAccounts("h").size() >= 2);
        assertEquals(1, accountDAO.getSearchAccounts("dre").size());
        assertEquals(0, accountDAO.getSearchAccounts("aravin").size());
        assertEquals(1, accountDAO.getSearchAccounts("Y").size());

        ArrayList<Account> found = accountDAO.getSearchAccounts("GDo");
        Account acc = found.get(0);
        assertEquals("ragdollboy", acc.getUsername());
        assertEquals("zheongxina.jpg", acc.getImage());
        assertEquals("567 764 284", acc.getPhoneNumber());
        assertEquals("16f75f3d4c89ca893d28a837f67ee5b332b0271e", acc.getPasswordHash());
    }

    @Test
    public void testGetAllAccounts() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        AccountDAO accountDAO = dbManager.getAccDAO();
        assertNotNull(accountDAO.getAllAccounts());
        assertTrue(accountDAO.getAllAccounts().size() > 0);
    }

    @Test
    public void testAddRemoveContains() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        AccountDAO accountDAO = dbManager.getAccDAO();

        int curNum = accountDAO.getAllAccounts().size();
        assertTrue(!accountDAO.contains("user1"));
        /** Test Add Account */
        accountDAO.addAccount("user1", "default.jpg", "568568568", "1234");
        accountDAO.addAccount("user2", "default.jpg", "568568000", "paroli");
        curNum += 2;
        assertEquals(curNum, accountDAO.getAllAccounts().size());
        assertTrue(accountDAO.contains("user1"));
        assertTrue(accountDAO.contains("user2"));

        /** Test Remove Account */
        accountDAO.removeAccount("user2");
        curNum--;
        assertTrue(!accountDAO.contains("user2"));
        assertEquals(curNum, accountDAO.getAllAccounts().size());
    }

    @Test
    public void testEditAndTryLogin() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        AccountDAO accountDAO = dbManager.getAccDAO();

        /** Test Edit Account */
        accountDAO.addAccount("user00", "default.jpg", "568568568", "1234");
        accountDAO.editAccount("user00", "axali user1", "default.jpg", "577577577", "");

        assertTrue(!accountDAO.contains("user00"));
        assertTrue(accountDAO.contains("axali user1"));

        accountDAO.addAccount("user2", "default.jpg", "568568777", "1234");
        accountDAO.editAccount("user2", "joni", "joni.jpg", "577577577", "2abd55e001c524cb2cf6300a89ca6366848a77d5");

        assertTrue(!accountDAO.contains("user2"));
        assertTrue(accountDAO.contains("joni"));

        /** Test Try Login */
        Account testAcc1 = accountDAO.tryLogin("joni", "5678");
        assertEquals("joni", testAcc1.getUsername());
        assertEquals("2abd55e001c524cb2cf6300a89ca6366848a77d5", testAcc1.getPasswordHash());
        assertEquals("joni.jpg", testAcc1.getImage());
        assertEquals("577577577", testAcc1.getPhoneNumber());

        assertTrue(accountDAO.tryLogin("nugzari", "pepela") == null);
        assertTrue(accountDAO.tryLogin("nshan", "minini") == null);

    }

    @Test
    public void testGetUserByName() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        AccountDAO accountDAO = dbManager.getAccDAO();

        Account testAcc = accountDAO.getUserByName("nshan");
        assertEquals("nshan", testAcc.getUsername());
        assertEquals("599 349 243", testAcc.getPhoneNumber());
        assertEquals("nerd.jpg", testAcc.getImage());
        assertEquals("c959e8dd5fa10a8707759417bc60e7d3b4ee6aed", testAcc.getPasswordHash());

        accountDAO.addAccount("sunshine", "sunshine.jpg", "540090222", "5678");
        Account testAcc1 = accountDAO.getUserByName("sunshine");
        assertEquals("sunshine", testAcc1.getUsername());
        assertEquals("540090222", testAcc1.getPhoneNumber());
        assertEquals("sunshine.jpg", testAcc1.getImage());
        assertEquals("2abd55e001c524cb2cf6300a89ca6366848a77d5", testAcc1.getPasswordHash());

        assertTrue(accountDAO.getUserByName("nodari") == null);
    }

    @Test
    public void testMakeModerator() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        AccountDAO accountDAO = dbManager.getAccDAO();

        accountDAO.addAccount("normalUser", "default.jpg", "599555599", "hehe");
        Account acc = accountDAO.getUserByName("normalUser");
        assertTrue(!acc.isModerator());

        accountDAO.makeModerator("normalUser");
        Account changedAcc = accountDAO.getUserByName("normalUser");
        assertTrue(changedAcc.isModerator());
    }

    @Test
    public void testUpdateInterests() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        AccountDAO accountDAO = dbManager.getAccDAO();
        InterestDAO interestDAO = dbManager.getInterestDAO();

        accountDAO.addAccount("dainteresebuliPiri", "default.jpg", "599234567", "paroli");
        Account acc = accountDAO.getUserByName("dainteresebuliPiri");
        Event e1 = new Event("barbie", "cinema", "barbis premiera", "2023-07-20", "20:00:00", "null", 1);
        interestDAO.AddInterest(acc, e1.getEvent_name());
        Event e2 = new Event("oppenheimer", "cinema", "openhaimeris premiera", "2023-07-20", "20:00", "null", 1);
        interestDAO.AddInterest(acc, e2.getEvent_name());

        Account changedAcc = accountDAO.getUserByName("dainteresebuliPiri");
        ArrayList<Event> events = interestDAO.getInterestedEvents(acc.getUsername());
        assertEquals("barbie", events.get(0).getEvent_name());

    }

    @Test
    public void testGetAchievementsAsHtml() throws Exception {
        SQLTablesManager dbManager = new SQLTablesManager("test");
        AccountDAO accountDAO = dbManager.getAccDAO();
        TicketDAO ticketDAO = dbManager.getTickDAO();

        Account acc1 = accountDAO.getUserByName("Dream");
        Account acc2 = accountDAO.getUserByName("GSagh");
        assertTrue(accountDAO.getAchievementsAsHtml(acc2, ticketDAO).contains("Fan Purchase:<br>This user has bought their first ticket</div>"));
        assertTrue(accountDAO.getAchievementsAsHtml(acc1, ticketDAO).contains("First Seller's Step:<br>This user has sold their first ticket</div>"));

        ticketDAO.changetickettobought("mfdhu53543", "GSagh");
        ticketDAO.changetickettobought("hgowl45394", "GSagh");
        ticketDAO.changetickettobought("fahiw74281", "GSagh");
        ticketDAO.changetickettobought("jhfbq32489", "GSagh");
        ticketDAO.changetickettobought("fahiw74282", "GSagh");
        assertEquals("<div class\"achievement\"><img class=\"achimg\" src=\"achievements/bought5.png\"><div class=\"achdesc\">Frequent Attendee:<br>This user has bought more than 5 tickets</div></div>\n\n", accountDAO.getAchievementsAsHtml(acc2, ticketDAO));
        assertEquals("<div class\"achievement\"><img class=\"achimg\" src=\"achievements/sold5.png\"><div class=\"achdesc\">Ticket Peddler:<br>This user has sold more than 5 tickets</div></div>\n\n", accountDAO.getAchievementsAsHtml(acc1, ticketDAO));

        ticketDAO.changetickettobought("bdhds83472", "GSagh");
        ticketDAO.changetickettobought("fasfw74281", "GSagh");
        ticketDAO.changetickettobought("hgowl45395", "GSagh");
        ticketDAO.changetickettobought("bdhds83473", "GSagh");
        ticketDAO.changetickettobought("jhfbq32490", "GSagh");
        assertTrue(accountDAO.getAchievementsAsHtml(acc1, ticketDAO).contains("<div class=\"achdesc\">Ticket Tycoon:<br>This user has made more than 1000 dollars selling tickets</div></div>"));
        assertTrue(accountDAO.getAchievementsAsHtml(acc2, ticketDAO).contains("/spent1000.png\"><div class=\"achdesc\">Grand Event Patron:<br>This user has spent more than 1000 dollars buying tickets</div></div>"));
    }

}