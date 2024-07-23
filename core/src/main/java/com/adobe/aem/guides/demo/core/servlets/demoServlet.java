package com.adobe.aem.guides.demo.core.servlets;

import java.io.IOException;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class,immediate=true)
  /**property={
   //   "sling.servlet.paths=/bin/name",
    "sling.servlet.resourceTypes=Demo/components/helloworld",
    "sling.servlet.selectors=two",
    "sling.servlet.extension=recent"
  }*/
  @SlingServletResourceTypes(
    resourceTypes = { "Demo/components/page","foundation/components/redirect"  },
    selectors = {"one","two","three"},
    extensions = {"txt","json",".xml"}
    )
public class demoServlet extends SlingSafeMethodsServlet{
   public void doGet(SlingHttpServletRequest req,SlingHttpServletResponse res)
   throws IOException
   {
    res.setContentType("application/json");
     JsonObjectBuilder obj=Json.createObjectBuilder();
      obj.add("FirstName","slingServlet");
      obj.add("task", "path/resourceType");
      res.getWriter().write(obj.build().toString());
   }
   //***************** retriving data from component **********
  //  String hello="/content/Demo/us/en/jcr:content/root/container/container/peace";
        // ResourceResolver resolver=request.getResourceResolver();
        // Resource resource=resolver.getResource(hello);
        // ValueMap vm=resource.getValueMap();
        // String fullname=vm.get("full name", String.class);
        // String mobileNumber=vm.get("mobilenumber",String.class);
        // response.getWriter().write("firstname : " +fullname);
        // response.getWriter().write(" mobilenumber :"+mobileNumber);
}
