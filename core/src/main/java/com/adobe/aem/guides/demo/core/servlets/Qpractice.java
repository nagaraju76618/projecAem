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
import com.day.cq.search.result.SearchResult;


@Component(service=Servlet.class,immediate=true,
property = {
    "sling.servlet.paths=/bin/qp"
})
public class Qpractice extends SlingAllMethodsServlet  {
   
    @Reference
    QueryBuilder queryBuilder;
  
    @Override
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp)
            throws ServletException, IOException {
                ResourceResolver resolver =req.getResourceResolver();
          
                Map<String,String>predicates=new HashMap<>();
                predicates.put("type","cq:Page");
                predicates.put("path","/content/we-retail");
                predicates.put("property.hits", "selective");
                predicates.put("property", "jcr:content/jcr:title");
                predicates.put("property.value", "English");
                predicates.put("p.limit", "-1");

             Query  query=queryBuilder.createQuery(PredicateGroup.create(predicates),req.getResourceResolver().adaptTo(Session.class));
           SearchResult  result=query.getResult();
              List<Hit> hits= result.getHits();
              JsonArrayBuilder jarray=Json.createArrayBuilder();
          for (Hit hit : hits)
           {
            Resource resource;
            JsonObjectBuilder json=Json.createObjectBuilder();
               try {
                resource=hit.getResource();
             Resource  content =resolver.getResource(resource.getPath()+"/jcr:content");
             if(content!=null){
           String  title=content.getValueMap().get("title",String.class);
           String path=content.getValueMap().get("sling:resourceType",String.class);
           json.add("title", title != null ? title : "Untitled") ;
           json.add("path", path!=null ? path : "hello");
        }
            } catch (RepositoryException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jarray.add(json);
            resp.getWriter().write(jarray.build().toString());

          }

    }
}
