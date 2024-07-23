package com.adobe.aem.guides.demo.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class TableForm {

    @Inject
    private String name;
    @Inject
    private String email;
    @Inject
    private String subject;
    @Inject
    private String message;

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getSubject() {
        return subject;
    }
    public String getMessage() {
        return message;
    }
   
}
