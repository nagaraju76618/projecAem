package com.adobe.aem.guides.demo.core.schedulers;

import com.day.cq.replication.Replicator;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
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
import com.adobe.aem.guides.demo.core.config.TaskConf;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(service = Runnable.class, immediate = true)
@Designate(ocd = TaskConf.class)
public class TaskSchedule implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(TaskSchedule.class);

    @Reference
    private Scheduler scheduler;

    @Reference
    private Replicator replicator;

    @Reference
    private ResourceResolverFactory resolverFactory;

    private String cronExpression;

    @Activate
    @Modified
    public void activate(TaskConf config) {
        this.cronExpression = config.expression();
        scheduleJob();
    }
    private void scheduleJob() {
        ScheduleOptions scheduleOptions = scheduler.EXPR(cronExpression);
        scheduleOptions.name("customised job");
        scheduleOptions.canRunConcurrently(false);
        try {
            scheduler.schedule(this, scheduleOptions);
            log.info("Scheduler job added with cron expression: {}", cronExpression);
        } catch (Exception e) {
            log.error("Error while scheduling the job", e);
        }
    }
    @Override
    public void run() {
        log.info("Custom Scheduler Job executed.");

        ResourceResolver resolver = null;
        try {
            // Create a ResourceResolver
            Map<String, Object> param = new HashMap<>();
            param.put(ResourceResolverFactory.SUBSERVICE, "naga");
            resolver = resolverFactory.getServiceResourceResolver(param);
            // Path to be replicated
            String pathToReplicate = "/content/Demo/us/en";
            // Replicate the content
            replicatePageAndChildren(resolver, pathToReplicate);
        } catch (Exception e) {
            log.error("Error during page replication", e);
        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
    }
    private void replicatePageAndChildren(ResourceResolver resolver, String path) {
        try {
            PageManager pageManager = resolver.adaptTo(PageManager.class);
            if (pageManager != null) {
                Page page = pageManager.getPage(path);
                if (page != null) {
                    replicatePage(resolver, page);
                    Iterator<Page> childPages = page.listChildren();
                    while (childPages.hasNext()) {
                        replicatePage(resolver, childPages.next());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error replicating page and children for path: {}", path, e);
        }
    }
    private void replicatePage(ResourceResolver resolver, Page page) {
        try {
            String pagePath = page.getPath();
            replicator.replicate(resolver.adaptTo(Session.class), ReplicationActionType.ACTIVATE, pagePath);
            log.info("Page at {} has been replicated.", pagePath);
        } catch (Exception e) {
            log.error("Error replicating page: {}", page.getPath(), e);
        }
    }
}
