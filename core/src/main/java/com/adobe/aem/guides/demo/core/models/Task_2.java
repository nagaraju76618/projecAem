package com.adobe.aem.guides.demo.core.models;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;


@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Task_2 {

    @Inject
    private String pathfield;
    @Inject
    private String textfield;
    @Inject
    private String checkbox;
    @ChildResource
    private List<Unit>multi;
    
    public List<Unit> getMulti() {
        return multi;
    }
    public String getPathfield() {
        return pathfield;
    }
    public String getTextfield() {
        return textfield;
    }
    public String getCheckbox() {
        return checkbox;
    }
}
