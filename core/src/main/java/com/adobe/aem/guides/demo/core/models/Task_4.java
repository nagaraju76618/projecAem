package com.adobe.aem.guides.demo.core.models;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Task_4 {
 
    @Inject
    private String lpath;
    @Inject
    private String mobileImg;
    @Inject
    private String logolink;
    @Inject
    private String checkbox;
    @Inject
    private String country;

    @ChildResource
    private List<Mf1>headlink;
    @ChildResource
    private List<Mf1>naviside;
    
    public String getLpath() {
        return lpath;
    }
    public String getMobileImg() {
        return mobileImg;
    }
    public String getLogolink() {
        return logolink;
    }
    public String getCheckbox() {
        return checkbox;
    }
    public String getCountry() {
        return country;
    }
    public List<Mf1> getHeadlink() {
        return headlink;
    }
    public List<Mf1> getNaviside() {
        return naviside;
    }

}
