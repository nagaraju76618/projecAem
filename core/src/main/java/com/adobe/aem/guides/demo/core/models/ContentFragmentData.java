package com.adobe.aem.guides.demo.core.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContentFragmentData {

    private static final Logger logger = LoggerFactory.getLogger(ContentFragmentData.class);

    @Self
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    private String cfPath;
    private Optional<ContentFragment> contentFragment;

    private String name;
    private String content;
    private String referencedText;
    private String referencedPath;

    @PostConstruct
    public void init() {
        cfPath = "/content/dam/contentfm/cf-1";  // Path to cf-1 instance
        Resource fragmentResource = resourceResolver.getResource(cfPath);
        contentFragment = Optional.ofNullable(fragmentResource.adaptTo(ContentFragment.class));

        if (contentFragment.isPresent()) {
            ContentFragment cf = contentFragment.get();

            // Retrieve fields from cf-1
            name = getElementContent("name");
            content = getElementContent("content");

            // Retrieve content reference field from cf-1, which should contain the path to cf-2
            String contentReference = getElementContent("content"); // Expecting path to cf-2 like /content/dam/contentfm-2/cf-2
            logger.debug("Content reference field from cf-1: {}", contentReference);

            // Load and retrieve fields from the referenced content fragment cf-2
            if (StringUtils.isNotEmpty(contentReference)) {
                Resource referencedFragmentResource = resourceResolver.getResource(contentReference);
                if (referencedFragmentResource != null) {
                    logger.debug("Referenced fragment resource found: {}", referencedFragmentResource.getPath());
                    Optional<ContentFragment> referencedFragment = Optional.ofNullable(referencedFragmentResource.adaptTo(ContentFragment.class));
                    
                    if (referencedFragment.isPresent()) { 
                        ContentFragment cf2 = referencedFragment.get();
                        referencedText = getElementContentFromFragment(cf2, "textfield");
                        referencedPath = getElementContentFromFragment(cf2, "pathfield");
                        logger.debug("Retrieved textfield from cf-2: {}", referencedText);
                        logger.debug("Retrieved pathfield from cf-2: {}", referencedPath);
                    } else {
                        logger.warn("Referenced Content Fragment could not be adapted to ContentFragment class.");
                    }
                } else {
                    logger.warn("Referenced fragment resource not found for path: {}", contentReference);
                }
            } else {
                logger.warn("Content reference field is empty or null.");
            }

        } else {
            logger.warn("Content Fragment not found at path: {}", cfPath);
        }
    }

    private String getElementContent(String elementName) {
        return contentFragment.map(cf -> cf.getElement(elementName))
                .map(ContentElement::getContent)
                .orElse(StringUtils.EMPTY);
    }

    private String getElementContentFromFragment(ContentFragment cf, String elementName) {
        return Optional.ofNullable(cf.getElement(elementName))
                .map(ContentElement::getContent)
                .orElse(StringUtils.EMPTY);
    }

    private String parseDate(Object date) {
        if (date instanceof Calendar) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, YYYY");
            return dateFormat.format(((Calendar) date).getTime());
        }
        return StringUtils.EMPTY;
    }

    // Getter methods for variables

    public String getCfPath() {
        return cfPath;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getReferencedText() {
        return referencedText;
    }

    public String getReferencedPath() {
        return referencedPath;
    }
}
 