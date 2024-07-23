package com.adobe.aem.guides.demo.core.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.demo.core.config.OsgiConfig;

@Component(immediate = true)
@Designate(ocd = OsgiConfig.class)
public class Display {

private String Text;
private String Url;

private static final Logger log=LoggerFactory.getLogger(Display.class);

@Activate
public  void activate(OsgiConfig osgiConfig){

    this.Text = osgiConfig.Text();
    this.Url=osgiConfig.Url();
    log.info(Text);
    log.info(Url);
}

public String getText() {
    return Text;
}

public String getUrl() {
    return Url;
}
@Deactivate
public void deactivate(){

    log.info("deactivation the log");
}
@Modified
public void modified(){
    log.info("this is modified");
}

}
