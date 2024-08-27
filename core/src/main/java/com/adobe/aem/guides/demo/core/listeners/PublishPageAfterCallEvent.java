package com.adobe.aem.guides.demo.core.listeners;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowService;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;

@Component(service=EventHandler.class,immediate = true,
property={
      EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC

})
public class PublishPageAfterCallEvent implements EventHandler{

    private static final Logger log=LoggerFactory.getLogger(PublishPageAfterCallEvent.class);

    @Reference
    private ResourceResolverFactory resolverFactory;
    @Reference
    private WorkflowService workflowService;

    String workflowModelPath = "/var/workflow/models/PageStart";

    @Override
    public void handleEvent(Event event) {
        
        ReplicationAction replicationAction = ReplicationAction.fromEvent(event);
        
        if (replicationAction != null && ReplicationActionType.ACTIVATE.equals(replicationAction.getType())) {
            String path = replicationAction.getPath();
            
            if (path != null && path.startsWith("/content")) {

                log.info("event handler triggered succesfully");
               
                log.info("Page published at path: {}", path);
                try {
                    startWorkflow(path);
                } catch (LoginException e) {
                   
                    e.printStackTrace();
                }
            }
        }
    }
    private void startWorkflow(String path) throws LoginException {
         Map<String, Object> params = new HashMap<>();
        params.put(ResourceResolverFactory.SUBSERVICE, "naga");

        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(params)) {
            Session session = resolver.adaptTo(Session.class);
            WorkflowSession workflowSession = workflowService.getWorkflowSession(session);
     
            WorkflowModel workflowModel = workflowSession.getModel(workflowModelPath);
            WorkflowData workflowData = workflowSession.newWorkflowData("JCR_PATH", path);
            workflowSession.startWorkflow(workflowModel, workflowData);

            log.info("Workflow started for payload: {}", path);
        } catch (WorkflowException e) {
            log.error("WorkflowException while starting workflow for payload: {}", path, e);
        }
    } 
}
    
