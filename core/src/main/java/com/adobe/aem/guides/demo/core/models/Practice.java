package com.adobe.aem.guides.demo.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class Practice {
    
    @Inject
    public String fname;

     @Inject
     @Default(values = "AskAdmin")
     public String lname;
     @Inject
     public String dob; 
     @Inject
    public String gender;
    @Inject
    public String checkbox;

   public String getCheckbox() {
        return checkbox;
    }
public String getfname()
   {
        return fname;
   }
    public String getLastName()
   {
        return lname;
    }
    public String getDob(){
     return dob;
    }
    public String getGender()
    {
        return gender;  
    }
    

} 
    