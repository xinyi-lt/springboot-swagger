# 定义http接口主要考虑一下几个部分
 http://10.0.4.62:8080/swagger-ui.html  为在开发环境搭建的一个demo，可以直接访问。
# 一、请求request参数
      
## 请求url
在spring-boot中，接口的url匹配使用@RequestMapping 注解进行url地址的配置

```
@RestController
@RequestMapping("/hello")
public class HelloWorldController {

	@RequestMapping("/world")
	@ApiOperation(value = "测试接口", notes = "测试接口详细描述")
	public String sayHello() {
		return "Hello,World!";
	}
}
```

如上所示，在项目中的 接口访问地址为 /hello/world ，在访问之前需要知道basePath,
访问路径如下 `[http://ip:port/basepath//hello/world，在spring-boot中默认的路径为/]`
## 请求协议
     这个可以不用考虑，开发环境一般使用http，线上根据实际情况可以使用nginx配置http或者https
## 请求参数 MIME Types（get除外）
请求或者应答数据的格式，也就是我们通常说的内容协商，在http请求request中，通过content-type 标识请求数据类型，使用accept标识需要的应答类型

@ResponseBody
@RequestMapping(value = "/show", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
                consumes=MediaType.APPLICATION_JSON_VALUE)
@ApiOperation(value = "测试接口", notes = "测试接口详细描述")
public String show(
        @ApiParam(required = true, name = "name", value = "姓名") @RequestParam(name = "name", required = true) String stuName) {
    return "success";
}
 
如上示例 接口路径 show  ，支持的请求方式为post接受的数据格式为json，应答的数据格式为json 。
说明：在spring-boot中  Consumes指明request数据格式  produces告指明response数据格式。
这里我们开发过程中请为了简单起见，建议全部设置为 json格式  produces = MediaType.APPLICATION_JSON_VALUE
## 数据常用传输方式（结合swagger）
一般http协议占用，传输传输大概有如下几种形式，在一下例子中，注意content-type参数
### get请求参数通过 ? key=value形式进行数据传输
参数接受使用@QueryParam  获取或者request.getParams()
在swagger中使用  paramType=query
```
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
```
通过swagger try it中显示如下如下请求应答报文：
```
Curl
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/requestTest/queryParam?name=111'
Request URL
http://localhost:8080/requestTest/queryParam?name=111
Request Headers
{
  "Accept": "application/json"
}
Response Body
{
  "code": "0000",
  "msg": "succuess",
  "body": {
    "id": null,
    "name": "111",
    "age": 0
  }
}
Response Code
200
Response Headers
{
  "date": "Fri, 28 Jul 2017 07:45:39 GMT",
  "transfer-encoding": "chunked",
  "content-type": "application/json;charset=UTF-8"
}
```
### 通过路径进行参数传输 /getUser/{userId}
    参数通过 @PathParam获取
@RequestMapping(value = { "/pathParam/{name}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
@ApiOperation(value = "验证path传参", notes = "")
@ApiImplicitParam(name = "name", value = "name通过path传参数", required = true, dataType = "String", paramType = "path")
public ResponseCode<User> testPathParam(@PathVariable("name") String name) {
    User user = new User();
    user.setName(name);
    return new ResponseCode<User>(user);
}
在swagger中效果如下：

```
GET /requestTest/pathParam/{name}
验证path传参
Response Class (Status 200)
OK
ModelExample Value
{
  "body": {
    "age": 0,
    "id": 0,
    "name": "string"
  },
  "code": "string",
  "msg": "string"
}

Response Content Type 
Parameters
Parameter	Value	Description	Parameter Type	Data Type
name	
(required)
name通过path传参数
path	string
```
```
Curl
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/requestTest/pathParam/111'
Request URL
http://localhost:8080/requestTest/pathParam/111
Request Headers
{
  "Accept": "application/json"
}
Response Body
{
  "code": "0000",
  "msg": "succuess",
  "body": {
    "id": null,
    "name": "111",
    "age": 0
  }
}
Response Code
200
Response Headers
{
  "date": "Fri, 28 Jul 2017 07:48:33 GMT",
  "transfer-encoding": "chunked",
  "content-type": "application/json;charset=UTF-8"
}
```
在swagger中 指明 paramType=path

### 表单form传输传输post请求中，即 application/x-www-form-urlencoded
可以通过@FormParam获取或者request.getParams()

 ```
   @RequestMapping(value = { "/formParam" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "验证form传参", notes = "")
	@ApiImplicitParam(name = "name", value = "name通过form传参数", required = true, dataType = "String", paramType = "form")
	public ResponseCode<User> testFormParam(HttpServletRequest request) {
		User user = new User();
		user.setName(request.getParameter("name"));
		return new ResponseCode<User>(user);
	}
```
在swagger中效果如下

```
POST /requestTest/formParam
验证form传参
Response Class (Status 200)
OK
ModelExample Value
{
  "body": {
    "age": 0,
    "id": 0,
    "name": "string"
  },
  "code": "string",
  "msg": "string"
}

Response Content Type 
Parameters
Parameter	Value	Description	Parameter Type	Data Type
name	
(required)
name通过form传参数
formData	string
```
```

Curl
curl -X POST --header 'Content-Type: application/x-www-form-urlencoded' --header 'Accept: application/json' -d 'name=test' 'http://localhost:8080/requestTest/formParam'
Request URL
http://localhost:8080/requestTest/formParam
Request Headers
{
  "Accept": "application/json"
}
Response Body
{
  "code": "0000",
  "msg": "succuess",
  "body": {
    "id": null,
    "name": "test",
    "age": 0
  }
}
Response Code
200
Response Headers
{
  "date": "Fri, 28 Jul 2017 07:50:20 GMT",
  "transfer-encoding": "chunked",
  "content-type": "application/json;charset=UTF-8"
}
```
在swagger中通过paramType=form指明
### 在通过jsonobject进行传输（也就是我们常用的json对象）
      参数接收通过@RequestBody 进行实体映射
   ```
   @RequestMapping(value = { "/bodyParam" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value = "验证body传参", notes = "")
	@ApiImplicitParam(name = "user", value = "user通过User传参数", required = true, dataType = "User", paramType = "body")
	public ResponseCode<User> testBodyParam(@RequestBody User user) {

		return new ResponseCode<User>(new User());
	}
```
     在swagger中的效果如下
     
```
     POST /requestTest/bodyParam
验证body传参
Response Class (Status 200)
OK
ModelExample Value
{
  "body": {
    "age": 0,
    "id": 0,
    "name": "string"
  },
  "code": "string",
  "msg": "string"
}

Response Content Type 
Parameters
Parameter	Value	Description	Parameter Type	Data Type
user	
(required)

Parameter content type: 
user通过User传参数
body	
ModelExample Value
{
  "age": 0,
  "id": 0,
  "name": "string"
}
Response Messages
```
  
```
 Curl请求demo
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
   "age": 0, \ 
   "id": 0, \ 
   "name": "string" \ 
 }' 'http://localhost:8080/requestTest/bodyParam'
Request URL
http://localhost:8080/requestTest/bodyParam
Request Headers
{
  "Accept": "application/json"
}
Response Body
{
  "code": "0000",
  "msg": "succuess",
  "body": {
    "id": null,
    "name": null,
    "age": 0
  }
}
Response Code
200
Response Headers
{
  "date": "Fri, 28 Jul 2017 07:53:02 GMT",
  "transfer-encoding": "chunked",
  "content-type": "application/json;charset=UTF-8"
}
```
    在swagger中通过 paramType=body指定
    
### 数据在http header传输数X-Request-ID: 77e1c83b-7bb0-437b-bc50-a7a58e5660ac
     参数通过 @HeaderParam获取或者request.getHeader
     在swagger中通过  paramType=header指定

           
# 请求response参数
       Httprequest比较简单，在spring-boot中，通过@ResponseBody 默认返回以body形式返回应答数据。并且接口状态与http code标准码进行数据应答，比如404位 not found
默认只有httpcode 为200 ok的时候，才返回body数据，如果要使用自定义的code ，可以在返回的实体bean中进行封装
如下返回ResponseCode<User>对象

Response Body
{
  "code": "0000",
  "msg": "succuess",
  "body": {
    "id": null,
    "name": null,
    "age": 0
  }
}

	```
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
```

如上例子，200成功返回userjson对象，500失败返回 errorCode json对象
也可以通过设置全局的返回 ：

```
// 当每个业务接口服务很多时，根据包路径分隔group
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).groupName("信贷")
				.useDefaultResponseMessages(true)// 是否使用默认 httpcode
				.globalResponseMessage(RequestMethod.GET, customerResponseMessage()) // 对get请求做出来
				.select().apis(RequestHandlerSelectors.basePackage("org.spring.springboot.web.credit")) // 注解扫描路径
				.paths(PathSelectors.any()).build();// 所有的路径匹配，如果要对特殊路径匹配可以在path里面通过正则进行配置
	}
```

# 使用实例（spring-boot restful）
## 1、添加springfox依赖
  ```
 <!-- Swagger -->
		<!--springfox的基本信息文档: http://springfox.github.io/springfox/docs/current/#architecture -->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>2.7.0</version>
		</dependency>

		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>2.7.0</version>
		</dependency>
```
## 2、按照springboot风格，在classptah根路径创建swagger document 解析类
  ```
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
```
## 3、配置接口说明（可选）
      这里面主要配置接口的参数说明，如果要做到对代码无侵入，可以省略这个步骤，前提是双方对代码字段约定好（可以以数据库为准，定义命名风格）
      	```
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
```
主要注解说明：
     * @ApiOperation 描述接口
     * @ApiImplicitParams 接口参数描述 多个参数
     * @ApiImplicitParam 单个接口参数描述 name 参数名，value参数描述 dataType 参数类型 paramType path,
     *                   query, body, header or form 具体区别在与reuqest contentType
     * 
     * @ApiIgnore 忽略接口 默认暴露controller下面的所有注解了@RequestMapping的方法
## 4、启动springboot，访问http://10.0.4.62:8080/swagger-ui.html  具体ip和端口根据项目部署定

# 参考文档
## Springfox整合swagger 官网文档
     http://springfox.github.io/springfox/docs/current/#springfox-spring-mvc-and-spring-boot
## Swagger 数据结构官方文档
    https://swagger.io/docs/specification/basic-structure/

