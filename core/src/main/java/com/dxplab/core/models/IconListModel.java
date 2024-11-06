package com.dxplab.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class IconListModel {

   
    @ChildResource
    private Resource iconList;

    private List<String> iconSrcList;

    @PostConstruct
    private void init() {
        iconSrcList = new ArrayList<>();
        if (iconList != null) {
            iconList.getChildren().forEach(childResource -> {
                String iconSrc = childResource.getValueMap().get("iconSrc", String.class);
                if (iconSrc != null) {
                    iconSrcList.add(iconSrc);
                }
            });
        }
    }

    public List<String> getIconSrcList() {
        return iconSrcList;
    }
}