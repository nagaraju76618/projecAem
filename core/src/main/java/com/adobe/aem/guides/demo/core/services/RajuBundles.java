package com.adobe.aem.guides.demo.core.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service =RajuBundles.class,immediate = true,name = "RajuBundles" )
public class RajuBundles {
    public static Logger log=LoggerFactory.getLogger(RajuBundles.class);
    @Activate
    public  void start(){
        log.info("raju bundle activated");
    }
    @Deactivate
    public  void stop(){
        log.info("deactivated raju");
    }
}
