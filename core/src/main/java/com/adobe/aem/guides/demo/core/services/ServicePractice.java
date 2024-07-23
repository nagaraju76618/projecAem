package com.adobe.aem.guides.demo.core.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service =ServicePractice.class,immediate = true,name="ServicePractice" )
public class ServicePractice {

    private static Logger log=LoggerFactory.getLogger(ServicePractice.class);
    @Activate
    public void started(){
        log.info("practice session started");
    }
    @Deactivate
    public void stopped(){
        log.error("error is happened ");
    }
    @Modified
    public void changed(){
        log.info("changes has been occured");
    }
}
