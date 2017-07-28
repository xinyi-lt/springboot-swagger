package org.spring.springboot.web.credit;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot HelloWorld 案例
 *
 * Created by bysocket on 16/4/26.
 */
@RestController
@RequestMapping("/hello")
public class HelloWorldController {

	@RequestMapping("/world")
	@ApiOperation(value = "测试接口", notes = "测试接口详细描述")
	public String sayHello() {
		return "Hello,World!";
	}

	@ResponseBody
	@RequestMapping(value = "/show", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
					consumes=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "测试接口", notes = "测试接口详细描述")
	public String show(
			@ApiParam(required = true, name = "name", value = "姓名") @RequestParam(name = "name", required = true) String stuName) {
		return "success";
	}
	
	// 这里指定RequestMethod，如果不指定Swagger会把所有RequestMethod都输出，在实际应用中，具体指定请求类型也使接口更为严谨。
		//我们通过@ApiOperation注解来给API增加说明
		//通过@ApiImplicitParams@ApiImplicitParam注解来给参数增加说明
}
