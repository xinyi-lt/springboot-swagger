package org.spring.springboot.bean;

/**
 * response 应答实体类
 * 
 * @author liulu
 *
 * @param <T>
 */
public class ResponseCode<T> {

	private String code;
	private String msg;
	private T body;

	public ResponseCode() {
	}

	public ResponseCode(String code, String msg, T body) {
		this.code = code;
		this.msg = msg;
		this.body = body;
	}
	
	/**
	 * 成功构造方法这里 code、msg可以使用枚举
	 * @param body
	 */
	public ResponseCode(T body) {
		this.code = "0000";
		this.msg = "succuess";
		this.body = body;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
}
