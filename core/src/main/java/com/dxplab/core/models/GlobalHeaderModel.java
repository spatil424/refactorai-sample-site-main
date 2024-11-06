package com.dxplab.core.models;

import javax.annotation.PostConstruct;

import com.adobe.cq.export.json.ExporterConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "dxplab/components/content/globalheader", adapters = GlobalHeaderModel.class)
public class GlobalHeaderModel {

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
            if (res != null && res.hasChildren()) {
                for (Resource childResource : res.getChildren()) {
                    processChildResource(childResource);
                }
            }
        }
    }

    private void processChildResource(Resource childResource) {
        if (childResource.getName().equals(JCR_CONTENT)) return;

        Resource subChildRes = childResource.getChild(MASTER_DATA_PATH);
        String headerDataPath = subChildRes.getValueMap().get("header_Data_Path", String.class);
        Resource pageResource = resolver.getResource(headerDataPath);

        if (pageResource != null) {
            PageDetails pageDetails = new PageDetails();
            pageDetails.setPageTitle(getTitleFromResource(pageResource));
            if(pageResource != null && pageResource.hasChildren()){
                populateChildPages(pageResource, pageDetails);
            }
            else {
                pageDetails.setPageUrl(pageResource.getPath());
            }
            
            pageDetailsList.add(pageDetails);
        }
    }

    private void populateChildPages(Resource pageResource, PageDetails pageDetails) {        
        for (Resource pageRes : pageResource.getChildren()) {
            if (!pageRes.getName().equals(JCR_CONTENT)) {
                ChildPage childPage = new ChildPage();
                childPage.setChildPageUrl(pageRes.getPath());
                childPage.setChildPageTitle(getTitleFromResource(pageRes));
                populateSubChildPages(pageRes, childPage);
                pageDetails.addChildPage(childPage);
            }
        }
    }

    private void populateSubChildPages(Resource pageRes, ChildPage childPage) {
        for (Resource subChildRes : pageRes.getChildren()) {
            if (!subChildRes.getName().equals(JCR_CONTENT)) {
                SubChildPage subChildPage = new SubChildPage();
                subChildPage.setChildPageUrl(subChildRes.getPath());
                subChildPage.setChildPageTitle(getTitleFromResource(subChildRes));
                childPage.addSubChildPage(subChildPage);
            }
        }
    }

    private String getTitleFromResource(Resource resource) {
        Resource contentResource = resource.getChild(JCR_CONTENT);
        return (contentResource != null) ? contentResource.getValueMap().get("jcr:title", String.class) : null;
    }

    public static class PageDetails {
        private String pageTitle;
        private String pageUrl;
        private List<ChildPage> childPageList = new ArrayList<>();

        public String getPageTitle() {
            return pageTitle;
        }

        public void setPageTitle(String pageTitle) {
            this.pageTitle = pageTitle;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
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
        private List<SubChildPage> subChildPageList = new ArrayList<>();

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

        public List<SubChildPage> getSubChildPageList() {
            return subChildPageList;
        }

        public void addSubChildPage(SubChildPage subChildPage) {
            this.subChildPageList.add(subChildPage);
        }
    }

    public static class SubChildPage {
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