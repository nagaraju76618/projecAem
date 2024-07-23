package com.adobe.aem.guides.demo.core.models;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables= Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PeaceMulti {
    @Inject
    public String name;
    @Inject
    public String date;

    @ChildResource
    private List<nestedPeace>nestedMulti;
    
    public List<nestedPeace> getNestedMulti() {
        return nestedMulti;
    }
    public String getName() {
        return name;
    }
    public String getDate() {
        return date;
    }
    
}
