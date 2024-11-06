package com.dxplab.core.models;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.wcm.core.components.models.Teaser;

@Model(adaptables = SlingHttpServletRequest.class, adapters = Teaser.class, resourceType = CarouselTeaserModel.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarouselTeaserModel implements Teaser {

    protected static final String RESOURCE_TYPE = "dxp/components/carouselteaser";
    
    @ValueMapValue
    private String dateTime;

    private String preTitle;

    private String teaserDescription;

    @Self @Via(type=ResourceSuperType.class)
    private Teaser teaser;

    public String getPreTitle() {
        preTitle = teaser != null ? teaser.getPretitle() : null;
        return preTitle;
    }

    public String getTeaserDescription() {
        teaserDescription = teaser != null ? teaser.getDescription() : null;
        return teaserDescription;
    }

    public String getDateTime() {
        if (dateTime != null) {
            OffsetDateTime parsedDateTime = OffsetDateTime.parse(dateTime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return parsedDateTime.format(formatter);
        }
        return null;
    }
}