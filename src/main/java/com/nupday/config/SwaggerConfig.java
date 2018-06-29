package com.nupday.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${nupday.swagger.enable}")
    private Boolean enable;

    @Value("${nupday.swagger.title}")
    private String title;

    @Value("${nupday.swagger.description}")
    private String description;

    @Value("${nupday.swagger.url}")
    private String url;

    @Value("${nupday.swagger.version}")
    private String version;

    @Value("${nupday.swagger.author}")
    private String author;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(enable ? PathSelectors.any() : PathSelectors.none())
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title(title)
            .description(description)
            .termsOfServiceUrl(url)
            .contact(author)
            .version(version)
            .build();
    }

}