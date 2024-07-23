package com.adobe.aem.guides.demo.core.models;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Multi {
    
    @Inject
    public String fullname;
   
    @Inject
    public String date;
    @Inject
    public String gender;
    @Inject
    public String mobilenumber;
    @Inject
    public String checkbox;

    @ChildResource
     private List<PeaceMulti>multifield;


    public String getDate() {
        return date;
    }
    public List<PeaceMulti> getMultifield() {
        return multifield;
    }
    public String getGender() {
        return gender;
    }
    public String getMobilenumber() {
        return mobilenumber;
    }
    public String getCheckbox() {
        return checkbox;
    }
    public String getFullname() {
        return fullname;
    }
   
   
}
