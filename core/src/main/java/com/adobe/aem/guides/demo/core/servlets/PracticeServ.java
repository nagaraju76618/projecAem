package com.adobe.aem.guides.demo.core.servlets;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(service = Servlet.class,immediate = true,
   property = {
    //"sling.servlet.paths=/bin/pp"
    "sling.servlet.resourceTypes=Demo/components/helloworld",
    "sling.servlet.selectors=two",
    "sling.servlet.extension=recent" 
   } ) 
public class PracticeServ extends SlingSafeMethodsServlet{
  
    private static Logger log=LoggerFactory.getLogger(PracticeServ.class) ;
    @Override  
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp)
            throws ServletException, IOException {
        
             resp.setContentType("application/json");
             JsonObjectBuilder  obj =Json.createObjectBuilder();
             obj.add("FirstName","practice");
             obj.add("inteview","saturday");
             obj.add("mode","offline/online");
             resp.getWriter().write(obj.build().toString());
             log.info("servlet has been working");
    }
}
