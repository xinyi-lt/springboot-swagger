package org.spring.springboot.web.loan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spring.springboot.bean.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users/easy")
// 通过这里配置使下面的映射都在/users下，可去除
public class UserEasyController {
/**
 * 对业务无侵入，只是没有参数的注释说明了
 */
	static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	public List<User> getUserList() {
		List<User> r = new ArrayList<User>(users.values());
		return r;
	}

	// path, query, body, header or form

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String postUser(@RequestBody User user) {
		users.put(user.getId(), user);
		return "success";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public User getUser(@PathVariable Long id) {
		return users.get(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String putUser(@PathVariable Long id, @RequestBody User user) {
		User u = users.get(id);
		u.setName(user.getName());
		u.setAge(user.getAge());
		users.put(id, u);
		return "success";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String deleteUser(@PathVariable Long id) {
		users.remove(id);
		return "success";
	}

}
