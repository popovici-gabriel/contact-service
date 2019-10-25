package com.ionos.domains.contact.configuration;

import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.builders.RequestHandlerSelectors.any;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class OpenApiConfiguration {

	@Bean
	// @formatter:off
	public Docket jobServiceApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("contact-service").useDefaultResponseMessages(false)
				.select().apis(any()).paths((regex("/api/.*"))).build().apiInfo(apiEndPointsInfo());
	}
	// @formatter:on

	// @formatter:off
	private ApiInfo apiEndPointsInfo() {
		return new ApiInfoBuilder().title("Contact Service API").description("Contact Service API")
				.contact(
						new Contact("Gabriel Popovici", "https://inside.1and1.org", "gabriel-sorin.popovici@ionos.com"))
				.license("1&1").licenseUrl("https://inside.1and1.org").version("1.0.0").build();
	}
	// @formatter:on
}
