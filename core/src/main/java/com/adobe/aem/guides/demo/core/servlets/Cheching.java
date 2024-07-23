package com.adobe.aem.guides.demo.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

@Component(
    service =  Servlet.class,
    property = {
        "sling.servlet.methods=POST",
        "sling.servlet.paths=/bin/deleteContentNode"
    })
public class Cheching extends SlingAllMethodsServlet {
     
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws  IOException {
        String path = request.getParameter("path");
        if (path == null || path.isEmpty()) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Path parameter is missing");
            return;
        }
         // Replace _jcr_content with jcr:content
         String modifiedPath = path.replace("_jcr_content", "jcr:content");

         // Print the received path and modified path
         response.getWriter().write("Received path: " + path + "\n");
         response.getWriter().write("Modified path: " + modifiedPath + "\n");
         ResourceResolver resolver = request.getResourceResolver();
         try {
             Resource resource = resolver.getResource(modifiedPath);
             if (resource != null) {
                 ModifiableValueMap valueMap = resource.adaptTo(ModifiableValueMap.class);
                 if (valueMap != null) {
                     // Remove the "cssClass" property if it exists
                     if (valueMap.containsKey("textfield")) {
                         valueMap.remove("textfield");
                     }
                      resolver.commit();
                    response.setStatus(SlingHttpServletResponse.SC_OK);
                    response.getWriter().write("Content node deleted and 'cssClass' property removed if it existed.");
                } else {
                    response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("Unable to adapt resource to ModifiableValueMap.");
                      }
                   } else {
                response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Content node not found.");
                 }
            } catch (PersistenceException e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("PersistenceException: " + e.getMessage());
    }   
}}
