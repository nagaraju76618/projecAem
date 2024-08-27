package com.adobe.aem.guides.demo.core.servlets;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component(service = Servlet.class, immediate = true,
        property = {
                "sling.servlet.paths=/bin/imageFolder"
        })
public class ImagesTriggerByJS extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(ImagesTriggerByJS.class);

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");

        JsonObject jsonResponse = new JsonObject();
        JsonArray imagesArray = new JsonArray();

        try {
            // Query to get assets
            Map<String, String> map = new HashMap<>();
            map.put("path", "/content/dam/images");
            map.put("type", DamConstants.NT_DAM_ASSET);
            map.put("p.limit", "-1");

            Query query = queryBuilder.createQuery(PredicateGroup.create(map), req.getResourceResolver().adaptTo(Session.class));
            SearchResult result = query.getResult();

            // Collect image data
            for (Hit hit : result.getHits()) {
                Asset asset = hit.getResource().adaptTo(Asset.class);
                if (asset != null) {
                    JsonObject imageObject = new JsonObject();
                    imageObject.addProperty("path", asset.getPath());
                    imageObject.addProperty("name", asset.getName());
                    imagesArray.add(imageObject);
                }
            }

            jsonResponse.add("imagePaths", imagesArray);

            // Add new nodes with properties for each image
            Session session = req.getResourceResolver().adaptTo(Session.class);
            if (session != null) {
                try {
                    Node rootNode = session.getNode("/apps/Demo/components/ImagesFolder/cq:dialog/content/items/column/items/DropDown/items");

                    // Create a new node for each image
                    for (int i = 0; i < imagesArray.size(); i++) {
                        JsonObject imageObject = imagesArray.get(i).getAsJsonObject();
                        String imageName = imageObject.get("name").getAsString();
                        String imagePath = imageObject.get("path").getAsString();

                        // Remove .jpg extension if present
                        String propertyName = imageName;
                        if (propertyName.endsWith(".jpg")) {
                            propertyName = propertyName.substring(0, propertyName.length() - 4);
                        } else if (propertyName.endsWith(".png")) {
                            propertyName = propertyName.substring(0, propertyName.length() - 4);
                        }

                        // Ensure unique node names if necessary
                        String nodeName = propertyName.replaceAll("[^a-zA-Z0-9]", "_"); // Replace invalid characters
                        Node imageNode = rootNode.addNode(nodeName, "nt:unstructured");

                        // Add property to the node
                        imageNode.setProperty("value", imagePath);
                        imageNode.setProperty("text", propertyName);
                    }

                    session.save();
                    log.info("Nodes created and properties added successfully");
                } catch (RepositoryException e) {
                    log.error("Error creating nodes or adding properties", e);
                }
            }

            log.info("IMAGE PATHS HAVE BEEN DISPLAYED");
        } catch (Exception e) {
            log.error("Error retrieving image paths", e);
            jsonResponse.addProperty("error", "Error retrieving image paths: " + e.getMessage());
        }

        res.getWriter().write(jsonResponse.toString());
    }
}
