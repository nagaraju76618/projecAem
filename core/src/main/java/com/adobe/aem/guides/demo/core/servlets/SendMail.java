package com.adobe.aem.guides.demo.core.servlets;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class,immediate = true,
property = {
    "sling.servlet.paths=/bin/send"
}
)
public class SendMail extends SlingAllMethodsServlet{

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
         
                String to = "admin@example.com"; // admin email
                String from = "user@example.com"; // sender email
                String host = "smtp.example.com"; // mail server

                Properties properties=System.getProperties();

              
    }
}
