package com.adobe.aem.guides.demo.core.config;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "PagePublishScheduler",description = "scheduler expression publish property")
public @interface PagePublish {

    @AttributeDefinition(
           name = "CronExpression",
           type = AttributeType.STRING,
           description="enter corn expression")
           String expression() default "0 0/1 * 1/1 * ? *";

           @AttributeDefinition(
            name = "Concurrent Execution",
            type = AttributeType.BOOLEAN, 
            description = "Allow concurrent executions")
          boolean concurrent() default false;
           
}
