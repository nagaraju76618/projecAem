package com.adobe.aem.guides.demo.core.workflow;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;


@Component(service = WorkflowProcess.class,immediate = true,
property={
    "process.label=PageStart Logging process step"
}
)
public class PageStart implements WorkflowProcess{
       
    private static final Logger log=LoggerFactory.getLogger(PageStart.class);
    @Reference
    ResourceResolverFactory resourceResolverFactory;
    
    @Override
    public void execute(WorkItem arg0, WorkflowSession arg1, MetaDataMap arg2)
     throws WorkflowException
      {
         log.info("this is PageStart process Step");
    }

}