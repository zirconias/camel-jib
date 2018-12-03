package com.example.demo;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.body;
import static org.apache.camel.model.rest.RestParamType.path;

@Component
public class CamelRouter extends RouteBuilder {

    @Autowired
    private Environment env;

    @Value("${camel.component.servlet.mapping.context-path}")
    private String contextPath;

    @Override
    public void configure() throws Exception {

        // @formatter:off

        // this can also be configured in application.properties
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .enableCORS(true)
                .port(env.getProperty("server.port", "8080"))
                .contextPath(contextPath.substring(0, contextPath.length() - 2))
                // turn on swagger api-doc
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "User API")
                .apiProperty("api.version", "1.0.0");

        rest("/users").description("User REST service")
          .consumes("application/json")
          .produces("application/json")

          .get().description("Find all users")
            .outType(User[].class)
            .responseMessage().code(200).message("All users successfully returned").endResponseMessage()
            .to("bean:userService?method=findUsers")

          .get("/{id}").description("Find user by ID")
            .outType(User.class)
            .param().name("id").type(path).description("The ID of the user").dataType("integer").endParam()
            .responseMessage().code(200).message("User successfully returned").endResponseMessage()
            .to("bean:userService?method=findUser(${header.id})")

          .put("/{id}").description("Update a user")
            .type(User.class)
            .param().name("id").type(path).description("The ID of the user to update").dataType("integer").endParam()
            .param().name("body").type(body).description("The user to update").endParam()
            .responseMessage().code(204).message("User successfully updated").endResponseMessage()
            .to("direct:update-user")

          .post().description("add user")
            .type(User.class)
            .param().name("body").type(body).description("The user to Add").endParam()
            .responseMessage().code(201).message("User successfully added").endResponseMessage()
            .to("direct:add-user")
        ;

        from("direct:update-user")
          .to("bean:userService?method=updateUser")
          .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(204))
          .setBody(constant(""));

        from("direct:add-user")
          .to("bean-validator://x")
          .to("bean:userService?method=addUser")
          .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
          .setBody(simple("${body}"))
        ;

        // @formatter:on
    }
}