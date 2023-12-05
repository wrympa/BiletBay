package Servlets;

import DAOs.AccountDAO;
import DAOs.ReportDAO;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ReportUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {
        if(httpServletRequest.getParameter("act").equals("report user")){
            ReportDAO reportDAO = (ReportDAO) httpServletRequest.getServletContext().getAttribute("reportdao");
            String torep = httpServletRequest.getParameter("torep");
            String repr = httpServletRequest.getParameter("repr");
            String desc = httpServletRequest.getParameter("reason");
            try {
                reportDAO.AddReport(torep, repr, desc);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            httpServletResponse.sendRedirect("user_page.jsp?id="+torep);
       } else if(httpServletRequest.getParameter("act").equals("approve report")){
            ReportDAO reportDAO = (ReportDAO) httpServletRequest.getServletContext().getAttribute("reportdao");
            AccountDAO accountDAO = (AccountDAO) httpServletRequest.getServletContext().getAttribute("accdao");
            String torep = httpServletRequest.getParameter("torep");
            try {
                reportDAO.RemoveReportsOf(torep);
                accountDAO.removeAccount(torep);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            httpServletResponse.sendRedirect("adminpage.jsp");
        } if(httpServletRequest.getParameter("act").equals("cancel report")){
            ReportDAO reportDAO = (ReportDAO) httpServletRequest.getServletContext().getAttribute("reportdao");
            String torep = httpServletRequest.getParameter("torep");
            String repr = httpServletRequest.getParameter("repr");
            try {
                reportDAO.RemoveReport(torep, repr);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            httpServletResponse.sendRedirect("user_page.jsp?id="+torep);
        }
    }
}
