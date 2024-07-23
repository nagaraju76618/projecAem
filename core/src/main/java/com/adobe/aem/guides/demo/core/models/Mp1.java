package com.adobe.aem.guides.demo.core.models;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Mp1 {

    @Inject
    private String text3;
    @Inject
    private String path;
    @ChildResource
    private List<Nm>tikk;
    
    public String getText3() {
        return text3;
    }
    public String getPath() {
        return path;
    }
    public List<Nm> getTikk() {
        return tikk;
    }
}
