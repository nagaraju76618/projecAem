package com.adobe.aem.guides.demo.core.models;

import java.util.List;
import javax.inject.Inject;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables =Resource.class)
public class Naga {
    
    @Inject
    private String fullname;

    @Inject
    private String fathername;

    @Inject
    private String phnumber;

    @Inject
    private String review;

    @ChildResource
    private List<Raju> sports;

    @ChildResource
    private List<Raju> education;

    public String getFullname() {
        return fullname;
    }
    public String getFathername() {
        return fathername;
    }
    public String getPhnumber() {
        return phnumber;
    }
    public String getReview() {
        return review;
    }
   
    
    public List<Raju> getSports() {
        return sports;
    }
    public List<Raju> getEducation() {
        return education;
    }
}
