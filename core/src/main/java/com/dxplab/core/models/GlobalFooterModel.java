package com.dxplab.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GlobalFooterModel {

    private static final String JCR_CONTENT = "jcr:content";
    private static final String MASTER_DATA_PATH = "jcr:content/data/master";

    @ValueMapValue
    private String pagePath;

    private List<PageDetails> pageDetailsList = new ArrayList<>();

    @SlingObject
    private ResourceResolver resolver;

    public List<PageDetails> getPageDetailsList() {
        return pageDetailsList;
    }

    @PostConstruct
    private void init() {
        if (pagePath != null) {
            Resource res = resolver.getResource(pagePath);
            if (res != null) {
                res.getChildren().forEach(this::processChildResource);
            }
        }
    }

    private void processChildResource(Resource childResource) {
        if (childResource.getName().equals(JCR_CONTENT)) return;

        Resource subChildRes = childResource.getChild(MASTER_DATA_PATH);
        String footerDataPath = subChildRes.getValueMap().get("footer_data_path", String.class);
        Resource pageResource = resolver.getResource(footerDataPath);

        if (pageResource != null) {
            PageDetails pageDetails = new PageDetails();
            pageDetails.setPageTitle(getTitleFromResource(pageResource));
            populateChildPages(pageResource, pageDetails);
            pageDetailsList.add(pageDetails);
        }
    }

    private void populateChildPages(Resource pageResource, PageDetails pageDetails) {
        pageResource.getChildren().forEach(pageRes -> {
            if (!pageRes.getName().equals(JCR_CONTENT)) {
                ChildPage childPage = new ChildPage();
                childPage.setChildPageUrl(pageRes.getPath());
                childPage.setChildPageTitle(getTitleFromResource(pageRes));
                pageDetails.addChildPage(childPage);
            }
        });
    }

    private String getTitleFromResource(Resource resource) {
        Resource contentResource = resource.getChild(JCR_CONTENT);
        return (contentResource != null) ? contentResource.getValueMap().get("jcr:title", String.class) : null;
    }

    public static class PageDetails {
        private String pageTitle;
        private List<ChildPage> childPageList = new ArrayList<>();

        public String getPageTitle() {
            return pageTitle;
        }

        public void setPageTitle(String pageTitle) {
            this.pageTitle = pageTitle;
        }

        public List<ChildPage> getChildPageList() {
            return childPageList;
        }

        public void addChildPage(ChildPage childPage) {
            this.childPageList.add(childPage);
        }
    }

    public static class ChildPage {
        private String childPageUrl;
        private String childPageTitle;

        public String getChildPageUrl() {
            if (childPageUrl.startsWith("/content")) {
                return childPageUrl + ".html";
            } else {
                return childPageUrl;
            }
        }

        public void setChildPageUrl(String childPageUrl) {
            this.childPageUrl = childPageUrl;
        }

        public String getChildPageTitle() {
            return childPageTitle;
        }

        public void setChildPageTitle(String childPageTitle) {
            this.childPageTitle = childPageTitle;
        }
    }
}