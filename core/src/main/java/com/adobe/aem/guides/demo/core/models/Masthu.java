package com.adobe.aem.guides.demo.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Masthu {
    
    @Inject
    private String hq;
    
    @Inject
    private String cgpa;

    public String getHq() {
        return hq;
    }
    public String getCgpa() {
        return cgpa;
    }
}
