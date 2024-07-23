package com.adobe.aem.guides.demo.core.workflow;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;


import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(service = WorkflowProcess.class, property = {"process.label=Task Page Created"})
public class TaskFlow implements WorkflowProcess {
    private static final Logger log = LoggerFactory.getLogger(TaskFlow.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) {
        String payloadPath = workItem.getWorkflowData().getPayload().toString();
        ResourceResolver resourceResolver = null;

        try {
            // Using service user to obtain resource resolver
            Map<String, Object> param = new HashMap<>();
            param.put(ResourceResolverFactory.SUBSERVICE, "naga");
            resourceResolver = resolverFactory.getServiceResourceResolver(param);

            Session session = resourceResolver.adaptTo(Session.class);
            Node pageNode = session.getNode(payloadPath);

            if (pageNode != null && pageNode.hasNode("jcr:content")) {
                Node contentNode = pageNode.getNode("jcr:content");
                String templatePath = contentNode.getProperty("cq:template").getString();

                Calendar date = Calendar.getInstance();
                if (!"/conf/Demo/settings/wcm/templates/page-content".equals(templatePath)) {
                    date.add(Calendar.DATE, -1);  
                }

                contentNode.setProperty("expired", date);
                session.save();
                log.info("Property 'expired' added to jcr:content of page at {}", payloadPath);
            } else {
                log.warn("jcr:content node not found for page at {}", payloadPath);
            }
        } catch (Exception e) {
            log.error("Error adding property to jcr:content of page at {}", payloadPath, e);
            throw new RuntimeException(e);
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }
    }
}
