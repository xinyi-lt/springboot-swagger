package org.spring.springboot.web.credit;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.spring.springboot.bean.ErrorCode;
import org.spring.springboot.bean.ResponseCode;
import org.spring.springboot.bean.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

/**
 * @ApiOperation 描述接口
 * @ApiImplicitParams 接口参数描述 多个参数
 * @ApiImplicitParam 单个接口参数描述 name 参数名，value参数描述 dataType 参数类型 paramType path,
 *                   query, body, header or form 具体区别在与reuqest contentType
 * 
 * @ApiIgnore 忽略接口 默认暴露controller下面的所有注解了@RequestMapping的方法
 * @author liulu
 *
 */
@RestController
@RequestMapping(value = "/requestTest")
// 通过这里配置使下面的映射都在/requestTest
public class DemoController {

	static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

	@RequestMapping(value = { "/queryParam" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "验证query传参", notes = "")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "name", value = "name参数", required = true, dataType = "String", paramType = "query")

	})
	// 默认不定义返回码http code 含义 比如 404位 not found，可以通过定义不同http code返回不同body数据
	@ApiResponses(value = { @ApiResponse(message = "访问成功", code = 200),
			@ApiResponse(message = "查完数据", code = 500, response = ErrorCode.class) })
	public ResponseCode<User> testQueryParam(@RequestParam("name") String name) {
		User user = new User();
		user.setName(name);
		return new ResponseCode<User>(user);
	}

	@RequestMapping(value = { "/pathParam/{name}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "验证path传参", notes = "")
	@ApiImplicitParam(name = "name", value = "name通过path传参数", required = true, dataType = "String", paramType = "path")
	public ResponseCode<User> testPathParam(@PathVariable("name") String name) {
		User user = new User();
		user.setName(name);
		return new ResponseCode<User>(user);
	}

	@RequestMapping(value = { "/formParam" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "验证form传参", notes = "")
	@ApiImplicitParam(name = "name", value = "name通过form传参数", required = true, dataType = "String", paramType = "form")
	public ResponseCode<User> testFormParam(HttpServletRequest request) {
		User user = new User();
		user.setName(request.getParameter("name"));
		return new ResponseCode<User>(user);
	}

	@RequestMapping(value = { "/bodyParam" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "验证body传参", notes = "")
	@ApiImplicitParam(name = "user", value = "user通过User传参数", required = true, dataType = "User", paramType = "body")
	public ResponseCode<User> testBodyParam(@RequestBody User user) {

		return new ResponseCode<User>(new User());
	}

	/**
	 * 忽略不暴露的接口
	 */
	@ApiIgnore
	@RequestMapping("/test")
	public void test() {

	}
}
