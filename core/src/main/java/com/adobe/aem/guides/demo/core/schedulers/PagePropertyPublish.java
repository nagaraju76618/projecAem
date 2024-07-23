package com.adobe.aem.guides.demo.core.schedulers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

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
import com.day.cq.replication.Replicator;
import com.day.cq.search.QueryBuilder;

@Component(service = Runnable.class, immediate = true)
@Designate(ocd = PagePublish.class)
public class PagePropertyPublish implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(PagePropertyPublish.class);

    @Reference
    private Replicator replicator;

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private Scheduler scheduler;

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
        ResourceResolver resourceResolver = null;
        try {
            Map<String, Object> param = new HashMap<>();
            param.put(ResourceResolverFactory.SUBSERVICE, "naga");
            resourceResolver = resolverFactory.getServiceResourceResolver(param);
            Session session = resourceResolver.adaptTo(Session.class);

            Calendar currentDate = Calendar.getInstance();
            Calendar previousDate = Calendar.getInstance();
            previousDate.add(Calendar.DATE, -1);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            String currentDateString = sdf.format(currentDate.getTime());
            String previousDateString = sdf.format(previousDate.getTime());

            // Paginate results to avoid reading too many nodes at once
            paginateAndProcess(session, currentDateString, ReplicationActionType.ACTIVATE);
            paginateAndProcess(session, previousDateString, ReplicationActionType.DEACTIVATE);

        } catch (Exception e) {
            log.error("Error during scheduled replication", e);
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }
    }

    private void paginateAndProcess(Session session, String dateString, ReplicationActionType actionType) throws Exception {
        int offset = 0;
        int limit = 1000; // Adjust as needed to avoid hitting the node traversal limit
        boolean moreResults = true;

        while (moreResults) {
            String queryString = "SELECT * FROM [cq:PageContent] AS s WHERE s.[expired] = '" + dateString + "' ORDER BY s.[jcr:created] ASC";
            Query query = session.getWorkspace().getQueryManager().createQuery(queryString, Query.JCR_SQL2);
            query.setOffset(offset);
            query.setLimit(limit);
            QueryResult result = query.execute();

            NodeIterator nodes = result.getNodes();
            moreResults = nodes.hasNext(); // If no more results, we can stop

            while (nodes.hasNext()) {
                Node node = nodes.nextNode();
                String pagePath = node.getPath().replace("/jcr:content", "");
                replicator.replicate(session, actionType, pagePath);
                log.info("Page {}: {}", actionType == ReplicationActionType.ACTIVATE ? "published" : "unpublished", pagePath);
            }

            offset += limit;
        }
    }
}
