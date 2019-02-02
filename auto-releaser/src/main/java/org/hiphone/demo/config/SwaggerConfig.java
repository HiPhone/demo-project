package org.hiphone.demo.config;

import org.hiphone.demo.constants.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author HiPhone
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(Constant.SWAGGER_BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(Constant.SWAGGER_TITLE)
                .description(Constant.SWAGGER_DESCRIPTION)
                .termsOfServiceUrl(Constant.SWAGGER_SERVICE_URL)
                .contact(contact())
                .version(Constant.SWAGGER_VERSION)
                .build();
    }

    private Contact contact() {
        return new Contact(Constant.SWAGGER_CONTACT_NAME, Constant.SWAGGER_CONTACT_URL, Constant.SWAGGER_CONTACT_EMAIL);
    }

}