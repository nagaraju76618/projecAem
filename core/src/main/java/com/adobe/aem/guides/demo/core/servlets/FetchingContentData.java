// package com.adobe.aem.guides.demo.core.servlets;

// import java.io.IOException;
// import java.util.HashMap;
// import java.util.Iterator;
// import java.util.Map;

// import javax.servlet.Servlet;
// import javax.servlet.ServletException;

// import org.apache.sling.api.SlingHttpServletRequest;
// import org.apache.sling.api.SlingHttpServletResponse;
// import org.apache.sling.api.resource.Resource;
// import org.apache.sling.api.servlets.SlingAllMethodsServlet;
// import org.osgi.service.component.annotations.Component;
// import org.osgi.service.component.annotations.Reference;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import com.adobe.cq.dam.cfm.ContentFragment;
// import com.adobe.cq.dam.cfm.ContentFragmentManager;
// import com.adobe.cq.dam.cfm.ContentElement;
// import com.google.gson.Gson;

// @Component(service = Servlet.class, immediate = true,
// property = {
//         "sling.servlet.paths=/bin/content",
//         "sling.servlet.selectors=model",
//         "sling.servlet.extension=json"
// })
// public class FetchingContentData extends SlingAllMethodsServlet {

//     private static final Logger log = LoggerFactory.getLogger(FetchingContentData.class);

//     @Reference
//     private ContentFragmentManager contentFragmentManager;

//     @Override
//     protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse res)
//             throws ServletException, IOException {

//         res.setContentType("application/json");
//         res.setCharacterEncoding("UTF-8");

//         try {
//             String[] fragmentPaths = {
//                     "/content/dam/practicecf/practicecf",
//                     "/content/dam/2_practicecf/2_practicecf"
//             };

//             Map<String, Object> fragmentsData = new HashMap<>();

//             for (String fragmentPath : fragmentPaths) {
//                 Resource fragmentResource = req.getResourceResolver().getResource(fragmentPath);
//                 if (fragmentResource != null) {
//                     ContentFragment fragment = fragmentResource.adaptTo(ContentFragment.class);
//                     if (fragment != null) {
//                         Map<String, Object> fragmentData = new HashMap<>();
//                         Iterator<ContentElement> elementsIterator = fragment.getElements();
//                         while (elementsIterator.hasNext()) {
//                             ContentElement element = elementsIterator.next();
//                             fragmentData.put(element.getName(), element.getContent());
//                         }
//                         fragmentsData.put(fragmentPath, fragmentData);
//                     }
//                 }
//             }

//             String json = new Gson().toJson(fragmentsData);
//             res.getWriter().write(json);
//         } catch (Exception e) {
//             log.error("Error fetching content fragments", e);
//             res.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//             res.getWriter().write("{\"error\":\"Unable to fetch content fragments\"}");
//         }
//     }
// }
