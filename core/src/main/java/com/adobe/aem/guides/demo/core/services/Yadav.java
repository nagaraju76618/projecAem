package com.adobe.aem.guides.demo.core.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service =Yadav.class,immediate = true,name="rajuYadav" )
public class Yadav {
 public static Logger log=LoggerFactory.getLogger(Yadav.class);
 @Activate
 public void activate(){
    log.info("activating");
 }
 @Deactivate
 public void deactivate(){
    log.info("deactivated");
 }
}
