package org.spring.springboot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

	// 当每个业务接口服务很多时，根据包路径分隔group
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).groupName("信贷")
				.useDefaultResponseMessages(true)// 是否使用默认 httpcode
				.globalResponseMessage(RequestMethod.GET, customerResponseMessage()) // 对get请求做出来
				.select().apis(RequestHandlerSelectors.basePackage("org.spring.springboot.web.credit")) // 注解扫描路径
				.paths(PathSelectors.any()).build();// 所有的路径匹配，如果要对特殊路径匹配可以在path里面通过正则进行配置
	}

	@Bean
	public Docket createRestApi1() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).groupName("车贷").select()
				.apis(RequestHandlerSelectors.basePackage("org.spring.springboot.web.loan"))

				.paths(PathSelectors.any()).build();
	}

	/**
	 * 
	 * api描述 支持的属性有version; title; description; termsOfServiceUrl; license;
	 * licenseUrl; Contact contact; l List<VendorExtension> vendorExtensions;
	 * 
	 * @return
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("信贷项目后端接口文档").description("").version("1.0").build();
	}

	/**
	 * http code response自定义
	 * 
	 * @param
	 * @return
	 */
	private List<ResponseMessage> customerResponseMessage() {

		List<ResponseMessage> list = new ArrayList<ResponseMessage>();
		list.add(new ResponseMessageBuilder().code(500).message("500 服务器内部错误").responseModel(new ModelRef("ErrorCode"))
				.build());// 500请求返回的modle
		list.add(new ResponseMessageBuilder().code(404).message("404 not found")
				.responseModel(new ModelRef("ErrorCode")).build());
		return list;
	}
}
