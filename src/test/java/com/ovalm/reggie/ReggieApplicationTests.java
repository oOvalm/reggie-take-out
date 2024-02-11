package com.ovalm.reggie;

import com.ovalm.reggie.utils.POP3Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class ReggieApplicationTests {

	@Autowired
	private POP3Util pop3Util;
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

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

}
