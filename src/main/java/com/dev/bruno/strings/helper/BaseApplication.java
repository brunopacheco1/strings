package com.dev.bruno.strings.helper;

import io.swagger.jaxrs.config.BeanConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("api")
public class BaseApplication extends Application {
	
	public BaseApplication() {
		BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setContact("brunopacheco1@yahoo.com");
        beanConfig.setTitle("String Set Service");
        beanConfig.setDescription("RESTFul project for String Set operations, developed in Java EE 7.");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("apps.bruno-pacheco.eng.br");
        beanConfig.setBasePath("/strings/api");
        beanConfig.setResourcePackage("com.dev.bruno.strings.resource");
        beanConfig.setScan(true);
	}
}