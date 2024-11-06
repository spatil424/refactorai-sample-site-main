package com.dxplab.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class InnerLinksModel {

    @Self
    private Resource resource;

    @ValueMapValue
    private String linkText;

    @ValueMapValue
    private String linkUrl;

    public String getLinkText() {
        return linkText;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

}