package com.adobe.aem.guides.demo.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = { Resource.class })
public class FormTable {

    @ChildResource
    private List<TableForm>dataTable;

    public List<TableForm> getDataTable() {
        return dataTable;
    }
}
