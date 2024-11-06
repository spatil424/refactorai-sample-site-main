package com.dxplab.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.api.resource.ValueMap;
import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TopLink {

    @Self
    private Resource resource;
    
    private ValueMap properties;
    
    @ValueMapValue
    private String linkImage;

    @ValueMapValue
    private String linkText;

    @ValueMapValue
    private String topLinkUrl;

   
    
    @PostConstruct
    protected void init() {
        properties = resource.getValueMap();
        linkImage = properties.get("linkImage", String.class);
        linkText = properties.get("linkText", String.class);
        topLinkUrl = properties.get("topLinkUrl", String.class);
    }

    public String getLinkImage() {
        return linkImage;
    }

    public String getLinkText() {
        return linkText;
    }

    public String getTopLinkUrl() {
        return topLinkUrl;
    }
}