package com.adobe.aem.guides.demo.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Unit {

    @Inject
    private String text2;
    @Inject
    private String  dob;
    
    public String getText2() {
        return text2;
    }
    public String getDob() {
        return dob;
    }
}
