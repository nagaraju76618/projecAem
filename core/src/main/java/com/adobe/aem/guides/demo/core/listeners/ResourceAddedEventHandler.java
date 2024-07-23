package com.adobe.aem.guides.demo.core.listeners;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowService;

@Component(service = EventHandler.class, immediate = true,
    property = {
        EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC
    })
public class ResourceAddedEventHandler implements EventHandler {

    private static final Logger log = LoggerFactory.getLogger(ResourceAddedEventHandler.class);

    @Reference
    private ResourceResolverFactory resourceFactory;

    @Reference
    private WorkflowService workflowService;

    @Override
    public void handleEvent(Event event) {
        log.info("Handler executed.");
        try {
            handleReplicationEvent(event);
        } catch (LoginException | RepositoryException | PersistenceException e) {
            log.error("Error handling replication event", e);
        }
    }

    private void handleReplicationEvent(Event event) throws LoginException, RepositoryException, PersistenceException {
        log.info("Handling replication event.");

        ReplicationAction action = ReplicationAction.fromEvent(event);
        String path = action.getPath();
        ReplicationActionType type = action.getType();
        log.info("Replication action occurred: {} for path: {}", type.getName(), path);

        switch (type) {
            case ACTIVATE:
                log.info("Content activated at path: {}", path);
                addProperty(path);
                startWorkflow(path);
                break;
            case DEACTIVATE:
                log.info("Content deactivated at path: {}", path);
                break;
        }
    }

    private void addProperty(String path) throws LoginException, RepositoryException, PersistenceException {
        Map<String, Object> params = new HashMap<>();
        params.put(ResourceResolverFactory.SUBSERVICE, "naga");

        try (ResourceResolver resolver = resourceFactory.getServiceResourceResolver(params)) {
            Resource resource = resolver.getResource(path + "/jcr:content");
            if (resource != null) {
                ModifiableValueMap modify = resource.adaptTo(ModifiableValueMap.class);
                if (modify != null) {
                    modify.put("changed", true);
                    resolver.commit();
                    log.info("Property 'changed' set to true for path: {}", path);
                } else {
                    log.warn("ModifiableValueMap is null for path: {}", path);
                }
            } else {
                log.warn("Resource not found for path: {}", path);
            }
        } catch (LoginException e) {
            log.error("LoginException while adding property for path: {}", path, e);
            throw e;
        } catch (PersistenceException e) {
            log.error("PersistenceException while adding property for path: {}", path, e);
            throw e;
        }
    }

    private void startWorkflow(String payloadPath) throws LoginException {
        Map<String, Object> params = new HashMap<>();
        params.put(ResourceResolverFactory.SUBSERVICE, "naga");

        try (ResourceResolver resolver = resourceFactory.getServiceResourceResolver(params)) {
            Session session = resolver.adaptTo(Session.class);
            WorkflowSession workflowSession = workflowService.getWorkflowSession(session);

            String workflowModelPath = "/var/workflow/models/CustomFlow";
            WorkflowModel workflowModel = workflowSession.getModel(workflowModelPath);
            WorkflowData workflowData = workflowSession.newWorkflowData("JCR_PATH", payloadPath);
            workflowSession.startWorkflow(workflowModel, workflowData);

            log.info("Workflow started for payload: {}", payloadPath);
        } catch (WorkflowException e) {
            log.error("WorkflowException while starting workflow for payload: {}", payloadPath, e);
        }
    } 
}
  //  private void triggerServlet(Event event) {
    //     String servletUrl = "http://localhost:4502/bin/demohandle"; // Replace with your servlet URL
    //     try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
    //         HttpGet request = new HttpGet(servletUrl);
    //         try (CloseableHttpResponse response = httpClient.execute(request)) {
    //             log.info("Servlet triggered with status: {}", response.getStatusLine().getStatusCode());
    //             log.info("servlet triggered sucessfully");
    //         }
    //     } catch (IOException e) {
    //         log.error("Error triggering servlet", e);
    //     }
    // } 
