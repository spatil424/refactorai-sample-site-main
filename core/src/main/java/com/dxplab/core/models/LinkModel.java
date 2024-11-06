package com.dxplab.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinkModel {

    @ValueMapValue
    private String heading;

    public String getHeading() {
        return heading;
    }

    @ChildResource
    private Resource linkList;

   
    private final List<InnerLinksModel> links = new ArrayList<>();

    
    public List<InnerLinksModel> getLinks() {
        return links;
    }

    @PostConstruct
    private void init() {
        if (linkList != null && linkList.hasChildren()) {
            for (Resource res : linkList.getChildren()) {
                InnerLinksModel innerLinksModel = res.adaptTo(InnerLinksModel.class);
                if (innerLinksModel != null) {
                    links.add(innerLinksModel);
                }
            }
        }
    }
}