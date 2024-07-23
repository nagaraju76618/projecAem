package com.adobe.aem.guides.demo.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class nestedPeace {
     @Inject
     public String number;
     @Inject
     public String address;
    public String getNumber() {
        return number;
    }
    public String getAddress() {
        return address;
    }
}
