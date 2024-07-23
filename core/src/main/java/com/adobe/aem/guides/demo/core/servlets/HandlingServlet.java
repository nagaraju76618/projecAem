package com.adobe.aem.guides.demo.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component( service = Servlet.class,immediate = true,
 property={
    "sling.servlet.paths=/bin/demohandle"
 }
)
public class HandlingServlet extends SlingAllMethodsServlet {
 
    private static final Logger log=LoggerFactory.getLogger(HandlingServlet.class);
    @Override
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse res)
            throws IOException {
          
                log.info("servlet triggered sucessfully");
                res.setContentType("application/json");
                res.getWriter().write("{\\\"message\\\":\\\"Servlet triggered successfully\\\"}");
    }
}
