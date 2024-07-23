package com.adobe.aem.guides.demo.core.models;

import javax.inject.Inject;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class)
public class HeaderComp {

    @ValueMapValue
    
    private String path;

    @ValueMapValue
    
    private String text;
    @Inject
   
    private boolean checkbox;


    public String getPath() {
        return path;
    }
    public String getText() {
        return text;
    }
    public boolean isCheckbox() {
        return checkbox;
    }
   
}
