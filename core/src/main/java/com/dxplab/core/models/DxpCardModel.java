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
public class DxpCardModel {

    @ValueMapValue
    private String textContent;

    @ChildResource
    private Resource buttonList;

    public String getTextContent() {
        return textContent;
    }

    List<InnerLinksModel> actionList = new ArrayList<>();

    public List<InnerLinksModel> getActionList() {
        return actionList;
    }

    @PostConstruct
    private void init() {
        if (buttonList != null) {
            buttonList.listChildren().forEachRemaining(res -> {
                InnerLinksModel innerLinksModel = res.adaptTo(InnerLinksModel.class);
                actionList.add(innerLinksModel);
            });
        }
    }
}