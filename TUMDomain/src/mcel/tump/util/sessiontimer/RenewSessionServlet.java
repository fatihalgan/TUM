package mcel.tump.util.sessiontimer;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class RenewSessionServlet extends HttpServlet
{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {        
        response.setContentType("text/xml");
        response.getOutputStream().println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        response.getOutputStream().println("<response>");
        response.getOutputStream().println("<result>Your Session Has Been Renewed...</result>");
        response.getOutputStream().println("</response>");
        response.getOutputStream().println("\n\n");
    }    
}
