package com.mbuyukasik.assignment.kalah.api.document;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class for swagger. 
 * It is used to generate REST service documentation
 * 
 * @author TTMBUYUKASIK
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	private ApiInfo apiInfo = new ApiInfo("Kalah Game", "This is an assignment of backbase. Configurable kalah game with default 6 stone.",
			"1.0.0", "",
			new Contact("Mehmet Buyukasik",
					"https://www.linkedin.com/in/mehmet-b%C3%BCy%C3%BCka%C5%9F%C4%B1k-04419571/",
					"mbuyukasik@gmail.com"),
			"", "", Collections.emptyList());

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors
				.basePackage("com.mbuyukasik.assignment.kalah.api"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo)
				.useDefaultResponseMessages(false);
                //.globalResponseMessage(RequestMethod.POST, getCustomizedResponseMessages());
	}
	
	/*
	private List<ResponseMessage> getCustomizedResponseMessages() {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder().code(201).message("Game created successfully!").responseModel(new ModelRef("CreateGameResponse")).build());
        responseMessages.add(new ResponseMessageBuilder().code(400).message("There is an active game!").responseModel(new ModelRef("ErrorResponse")).build());
        return responseMessages;
    }
    */
	
}
