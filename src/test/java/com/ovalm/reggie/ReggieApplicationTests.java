package com.ovalm.reggie;

import com.ovalm.reggie.entity.User;
import com.ovalm.reggie.service.UserService;
import com.ovalm.reggie.utils.POP3Util;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.sql.DataSource;

@SpringBootTest
class ReggieApplicationTests {

	@Autowired
	private POP3Util pop3Util;
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	private UserService userService;

	@Test
	void testEmail() {
		try {
			pop3Util.sendMessage("1834842489@qq.com", 12345);
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	void TestRedis() {
		ValueOperations<Object, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set("111", "222");
		String res = (String) valueOperations.get("111");
		System.out.println(res);
	}
	@Resource
	private DataSource dataSource;
	@Test
	void TestShardingsphere() {
		User user = new User();
		user.setId(1111L);
		user.setName("11111");
		user.setEmail("192321321@qq.com");
		userService.save(user);
	}

}
