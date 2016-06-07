package org.familysearch.paas;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;


public class DisplayEnvironmentServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setStatus(HttpServletResponse.SC_OK);
    final PrintWriter writer = resp.getWriter();
    try {
      System.getProperties().list(writer);
    }
    finally {
      writer.close();
    }
  }
}
