package com.adobe.aem.guides.demo.core.models;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Mf1 {

    @Inject
    private String name;
    @Inject
    private String Img;
    @Inject
    private String desktop;
    @Inject
    private String mobileIcon;
    @ChildResource
    private List<Nf1>urlNavigat;
    
    public String getName() {
        return name;
    }
    public String getImg() {
        return Img;
    }
    public String getDesktop() {
        return desktop;
    }
    public String getMobileIcon() {
        return mobileIcon;
    }
    public List<Nf1> getUrlNavigat() {
        return urlNavigat;
    }
}
