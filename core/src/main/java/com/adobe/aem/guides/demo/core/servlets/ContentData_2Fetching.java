package com.adobe.aem.guides.demo.core.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.ContentFragmentManager;
import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentVariation;
import com.google.gson.Gson;

@Component(service = Servlet.class, immediate = true,
property = {
        "sling.servlet.paths=/bin/Data_2",
        "sling.servlet.selectors=model",
        "sling.servlet.extension=json"
})
public class ContentData_2Fetching extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(ContentData_2Fetching.class);

    @Reference
    private ContentFragmentManager contentFragmentManager;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        try {
            String[] fragmentPaths = {
                    "/content/dam/practicecf/practicecf",
                    "/content/dam/2_practicecf/2_practicecf"
            };

            Map<String, Object> fragmentsData = new HashMap<>();

            for (String fragmentPath : fragmentPaths) {
                Resource fragmentResource = req.getResourceResolver().getResource(fragmentPath);
                if (fragmentResource != null) {
                    ContentFragment fragment = fragmentResource.adaptTo(ContentFragment.class);
                    if (fragment != null) {
                        Map<String, Object> fragmentData = new HashMap<>();
                        Iterator<ContentElement> elementsIterator = fragment.getElements();
                        while (elementsIterator.hasNext()) {
                            ContentElement element = elementsIterator.next();
                            Map<String, String> elementData = new HashMap<>();
                            elementData.put("master", formatContent(element.getContent()));

                            // Add variations
                            Iterator<ContentVariation> variationsIterator = element.getVariations();
                            while (variationsIterator.hasNext()) {
                                ContentVariation variation = variationsIterator.next();
                                String variationName = variation.getName();
                                String variationContent = formatContent(variation.getContent());
                                elementData.put(variationName, variationContent);
                            }

                            fragmentData.put(element.getName(), elementData);
                        }
                        fragmentsData.put(fragmentPath, fragmentData);
                    }
                }
            }

            String json = new Gson().toJson(fragmentsData);
            res.getWriter().write(json);
        } catch (Exception e) {
            log.error("Error fetching content fragments", e);
            res.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("{\"error\":\"Unable to fetch content fragments\"}");
        }
    }

    private String formatContent(Object content) {
        if (content instanceof Calendar) {
            return dateFormat.format(((Calendar) content).getTime());
        }
        return content.toString();
    }
}
