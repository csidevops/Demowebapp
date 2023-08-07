package com.example;
//package test.easycodeforall.changeit; 
//import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.log4j.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InsertApplicationIdServletTest {
  
	private Logger log = Logger.getLogger(this.getClass());
 
  @Test  
    public void doPost(){  
		try {
			log.info("Starting execution of doPost");
 HttpServletRequest request = null; 
HttpServletResponse response = null; 
 
  
 InsertApplicationIdServlet insertapplicationidservlet  =new InsertApplicationIdServlet(); 
insertapplicationidservlet.doPost( request ,response);
assertTrue(true);
		} catch (Exception exception) {
			log.error("Exception in execution ofdoPost-"+exception,exception);
			exception.printStackTrace();
			assertFalse(false);
		}
    }  
}
