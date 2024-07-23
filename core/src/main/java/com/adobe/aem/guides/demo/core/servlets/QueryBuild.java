package com.adobe.aem.guides.demo.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;

@Component(service = Servlet.class,immediate = true,
property = {
    "sling.servlet.paths=/bin/raju"
})
public class QueryBuild extends SlingAllMethodsServlet {
    @Reference                  //more than one service we write this annotation
    QueryBuilder queryBuilder;  // we write query we can get one servie querybuilder
    @Override
    public void doGet(SlingHttpServletRequest req, SlingHttpServletResponse res)
            throws ServletException, IOException {
              ResourceResolver resourceResolver=req.getResourceResolver();
                                // for writing key and value we write hashmap
      Map<String,String>predicate=new HashMap<>();
                                // we define our query not create
        predicate.put("type","cq:Page");
        predicate.put("path","/content/we-retail");
        predicate.put("p.hits","selective");
        predicate.put("property","jcr:content/jcr:title");
        predicate.put("p.properties","jcr:content/cq:template jcr:content/sling:resourceType");
        predicate.put("p.limit","-1");
                                // Create the query using QueryBuilder and execute it.
        Query query= queryBuilder.createQuery(PredicateGroup.create(predicate),req.getResourceResolver().adaptTo(Session.class));
                                // for executing Query 
      com.day.cq.search.result.SearchResult  result=query.getResult();
                                                // after that we need hits
         List<Hit>  hits  = result.getHits();  // for getting all hits here
              JsonArrayBuilder jsonarray =Json.createArrayBuilder();
         for(Hit hit:hits)                    // for getting oneby one hits only
         {      
         JsonObjectBuilder  jsonobj= Json.createObjectBuilder();
              Resource resource;
                try {
                  resource = hit.getResource();
                  Resource  content= resourceResolver.getResource(resource.getPath()+"/jcr:content");
                  if(content!=null) {  
                String title= content.getValueMap().get("title",String.class);
                String resourceType =content.getValueMap().get("sling:resourceType",String.class);
                   jsonobj.add("title", title != null ? title : "Untitled") ;
                   jsonobj.add("resourceType", resourceType!=null ? resourceType : "hello");
                  }     
                } catch (RepositoryException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
               jsonarray.add(jsonobj);
         }
         res.getWriter().write(jsonarray.build().toString());
    }  
}
