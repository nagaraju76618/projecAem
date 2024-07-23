package com.adobe.aem.guides.demo.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "osgiSchedule",description = "configuration and schedule")
public @interface Scheduledemo {

    @AttributeDefinition(
                         name = "ScheduleName",
                         type=AttributeType.STRING,
                         description = "enter schedule name")
                  String ScheduleName() default "Raju";
   @AttributeDefinition(
                         name = "Concurent",
                         type=AttributeType.BOOLEAN,
                         description = "select Concurent value")
                  boolean Concurent() default false;  
    @AttributeDefinition(
                         name = "Enabled",
                         type = AttributeType.BOOLEAN,
                         description = "enabled the schedule" ) 
                    boolean Enabled() default true;
     @AttributeDefinition(
                          name = "cornExpression",
                          type = AttributeType.STRING,
                          description = "enter your corn expression")
                    String Expression() default "0 0/1 * 1/1 * ? *" ;
                                       
}
