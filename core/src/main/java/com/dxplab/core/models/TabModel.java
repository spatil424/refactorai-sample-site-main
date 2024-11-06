package com.dxplab.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabModel {

    private static final String JCR_CONTENT = "jcr:content";
    private static final String MASTER_DATA_PATH = "jcr:content/data/master";

    @ValueMapValue
    private String path;

    @SlingObject
    private ResourceResolver resolver;

    private final List<TabField> tabData = new ArrayList<>();

    public List<TabField> getTabData() {
        return Collections.unmodifiableList(tabData);
    }
    
    @PostConstruct
    private void init() {
        Optional.ofNullable(path)
                .map(resolver::getResource)
                .ifPresent(this::processResource);
    }

    private void processResource(Resource resource) {
        for (Resource childResource : resource.getChildren()) {
            if (!JCR_CONTENT.equals(childResource.getName())) {
                Resource subResource = resolver.getResource(childResource.getPath());
                if (subResource != null && subResource.hasChildren()) {
                    for (Resource subChildResource : subResource.getChildren()) {
                        if (!JCR_CONTENT.equals(subChildResource.getName())) {
                            Resource subChildRes = subChildResource.getChild(MASTER_DATA_PATH);
                            if (subChildRes != null) {
                                TabField tabField = subChildRes.adaptTo(TabField.class);
                                if (tabField != null) {
                                    tabData.add(tabField);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}