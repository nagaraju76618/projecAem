package com.adobe.aem.guides.demo.core.schedulers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.demo.core.config.PagePublish;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Designate(ocd = PagePublish.class)
@Component(service = Runnable.class, immediate = true)
public class PagePropertyPublish implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(PagePropertyPublish.class);

    @Reference
    private Scheduler scheduler;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private Replicator replicator;

    private static final String SERVICE_USER = "naga";

    private String expression;
    private Boolean concurrent;

    @Activate
    @Modified
    protected void activate(PagePublish page) {
        this.expression = page.expression();
        this.concurrent = page.concurrent();
        scheduleJob();
    }

    private void scheduleJob() {
        ScheduleOptions schedulerOption = scheduler.EXPR(expression);
        schedulerOption.name("PagePropertyPublishJob");
        schedulerOption.canRunConcurrently(concurrent);
        scheduler.schedule(this, schedulerOption);
    }

    @Override
    public void run() {
        log.info("Scheduler is running in present date");
        log.info("My cron expression in present date: " + expression);
        
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = getServiceResourceResolver();
            if (resourceResolver != null) {
                handlePageReplication(resourceResolver);
            }
        } catch (LoginException e) {
            log.error("Failed to get service resource resolver", e);
        } catch (ReplicationException e) {
            log.error("ReplicationException occurred", e);
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }
    }

    private ResourceResolver getServiceResourceResolver() throws LoginException {
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER);
        return resourceResolverFactory.getServiceResourceResolver(param);
    }
      // PUBLISH AND UNPUBLISH EXPIRED PROPERTY PAGE WITH CURRENT &PREVIOUS DATE.
    private void handlePageReplication(ResourceResolver resourceResolver) throws ReplicationException {
        try {
            Session session = resourceResolver.adaptTo(Session.class);
            if (session == null) {
                log.error("Could not obtain a JCR session.");
                return;
            }

            String queryString = "SELECT * FROM [cq:PageContent] AS content WHERE ISDESCENDANTNODE(content, '/content/Demo/us/en') AND content.[expired] IS NOT NULL";
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            Query query = queryManager.createQuery(queryString, Query.JCR_SQL2);
            QueryResult result = query.execute();

            javax.jcr.NodeIterator nodes = result.getNodes();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            String currentDateStr = sdf.format(new Date());

            while (nodes.hasNext()) {
                Node node = nodes.nextNode();
                if (node.hasProperty("expired")) {
                    String expired = node.getProperty("expired").getString();
                    PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
                    Page page = pageManager.getContainingPage(node.getPath());
                    
                    if (page != null) {
                        if (expired.startsWith(currentDateStr.substring(0, 10))) {
                            // Publish the page
                            replicator.replicate(session, ReplicationActionType.ACTIVATE, page.getPath());
                            log.info("Published page: " + page.getPath());
                        } else if (expired.compareTo(currentDateStr) < 0) {
                            // Unpublish the page
                            replicator.replicate(session, ReplicationActionType.DEACTIVATE, page.getPath());
                            log.info("Unpublished page: " + page.getPath());
                        }
                    }
                }
            }
        } catch (RepositoryException e) {
            log.error("Error while querying and replicating pages", e);
        }
    }
}