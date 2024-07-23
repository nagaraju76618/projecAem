package com.adobe.aem.guides.demo.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.demo.core.config.Scheduledemo;

@Component(service =Runnable.class,immediate = true )
@Designate(ocd = Scheduledemo.class)
public class NagSch implements Runnable{

    private static final Logger log=LoggerFactory.getLogger(NagSch.class);
    @Reference
    private Scheduler scheduler;
    private String ScheduleName;
    private boolean Concurent;
    private boolean Enabled;
    private String Expression;
  @Activate
  @Modified
   protected void activate(Scheduledemo demo){
       ScheduleName =demo.ScheduleName();
       Concurent  =demo.Concurent();
       Enabled=demo.Enabled();
      Expression =demo.Expression();    
     if (Enabled){      
        addScheduler(demo);
    } else {
        removeScheduler();
    }  
   }
    private void removeScheduler() {
        log.info("Removing scheduler with name: {}", ScheduleName);
        scheduler.unschedule(ScheduleName);
}
    private void addScheduler(Scheduledemo demo) {
        ScheduleOptions scheduleroption= scheduler.EXPR(Expression);
        scheduleroption.name(ScheduleName);
        scheduleroption.canRunConcurrently(Concurent);
  log.info("Adding scheduler with name: {}, expression: {}, concurrent: {}", ScheduleName, Expression, Concurent);
        scheduler.schedule(this, scheduleroption);
}
    @Override
    public void run() {
       log.info("Running scheduled task: {}", ScheduleName);
    }

}
