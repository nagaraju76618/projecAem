package com.adobe.aem.guides.demo.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;


@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ChildPage {

    private String title;
    private String description;

    public ChildPage(Resource resource) {
        this.title = resource.getValueMap().get("jcr:title", String.class);
        this.description = resource.getValueMap().get("jcr:description", String.class);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

