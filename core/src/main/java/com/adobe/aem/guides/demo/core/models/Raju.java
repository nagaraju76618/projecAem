package com.adobe.aem.guides.demo.core.models;

import java.util.List;
import javax.inject.Inject;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Raju {
    
    @Inject
    private String game;
    
    @Inject
    private String music;

    @Inject
    private String degree;

    public String getGame() {
        return game;
    }
    public String getMusic() {
        return music;
    }
    public String getDegree() {
        return degree;
    }
    @ChildResource
    private List<Masthu> qualification;

    public List<Masthu> getQualification() {
        return qualification;
    }
}
