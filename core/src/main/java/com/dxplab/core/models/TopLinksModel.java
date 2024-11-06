package com.dxplab.core.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TopLinksModel {

    @ChildResource
    private Resource topLinks;

    private List<TopLink> topLinksList;

    public  List<TopLink> getTopLinksList() {
        return topLinksList != null ? topLinksList : new ArrayList<>();
    }

    @PostConstruct
    private void init() {
        topLinksList = new ArrayList<>();
        if (topLinks != null) {
            topLinks.getChildren().forEach(childRes -> {
                if (!"jcr:content".equals(childRes.getName())) { 
                    TopLink topLink = childRes.adaptTo(TopLink.class);
                    if (topLink != null) {
                        topLinksList.add(topLink);
                    }
                }
            });
        }
    }
}