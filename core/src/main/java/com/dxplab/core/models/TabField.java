package com.dxplab.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabField {

    @Self
    private Resource resource;

    @ValueMapValue
    private String carImage;

    @ValueMapValue
    private String engine_capacity;

    @ValueMapValue
    private String page_Name;

    @ValueMapValue
    private String page_url;

    @ValueMapValue
    private String price;

    @ValueMapValue
    private String transmission_Available;

    public String getCarImage() {
        return carImage;
    }

    public String getEngine_capacity() {
        return engine_capacity;
    }

    public String getPage_Name() {
        return page_Name;
    }

    public String getPage_url() {
        return page_url;
    }

    public String getPrice() {
        return price;
    }

    public String getTransmission_Available() {
        return transmission_Available;
    }

}