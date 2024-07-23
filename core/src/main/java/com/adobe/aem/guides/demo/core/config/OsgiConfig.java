package com.adobe.aem.guides.demo.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "OsgiConfig",description = "this is osgi configuration")
public @interface OsgiConfig {

   @AttributeDefinition(
      name = "Text",
      description="enter text field")
      String Text() default "Sample Text";
    
    @AttributeDefinition(
      name = "Url",
      description = "Please attach Url")
    String Url() default "/example/url";
}
