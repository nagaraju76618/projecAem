package com.adobe.aem.guides.demo.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Task1 {

    @ValueMapValue
    private String pathfield;

    @SlingObject
    private ResourceResolver resourceResolver;
   
    private List<ChildPage>child=new ArrayList<>();

    @PostConstruct
     public void init(){
        if(pathfield !=null){
         Resource  parentResource = resourceResolver.getResource(pathfield);
         if(parentResource !=null){
         for( Resource childResource:parentResource.getChildren()) {
            ChildPage pagechild = new ChildPage(childResource);
            child.add(pagechild);
        }
      }
    }
}
    public String getPathfield() {
        return pathfield;
    }
    public List<ChildPage> getChild() {
        return child;
    }

  public static class ChildPage{

    private String title;
    private String description;

    public ChildPage(Resource resource){
        
    this.title  =resource.getValueMap().get("jcr:title",String.class);
    this.description=resource.getValueMap().get("jcr:description",String.class);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }   
  }
}
