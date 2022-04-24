package com.training.portal.configuration;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc; 
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class Swagger {

    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).
                select().apis(RequestHandlerSelectors.basePackage("com.training.portal")).paths(PathSelectors.any())
                .build().pathMapping("/").apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("jahnavi", "@jahnavi", "jahnavi.adi@gmail.com");
        Collection<VendorExtension> vendorExtensions = new ArrayList<>();
        return new
                ApiInfo("Online exam portal", "Examination application", "1.0",
                "-", contact, "Owned by @jahnavi", "-", vendorExtensions);
    }
}
