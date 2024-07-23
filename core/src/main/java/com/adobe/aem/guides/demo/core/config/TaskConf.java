package com.adobe.aem.guides.demo.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "TaskConfig", description = "This is a scheduler task configuration provided by the user.")
public @interface TaskConf {

    @AttributeDefinition(
        name = "CRON Expression",
        type = AttributeType.STRING,
        description = "Enter CRON expression"
    )
    String expression() ;
}
