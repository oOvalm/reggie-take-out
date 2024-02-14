package com.ovalm.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.cj.Query;
import com.mysql.cj.util.StringUtils;
import com.ovalm.reggie.common.R;
import com.ovalm.reggie.entity.User;
import com.ovalm.reggie.service.UserService;
import com.ovalm.reggie.utils.POP3Util;
import com.ovalm.reggie.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private POP3Util pop3Util;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping("/sendMsg")
    public R sendMsg(@RequestBody User user) {
        String email = user.getEmail();
        if(!StringUtils.isNullOrEmpty(email)){
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            log.info("验证码：{}", code);
            try {
//                pop3Util.sendMessage(email, code);

//                session.setAttribute(email, code);
                redisTemplate.opsForValue().set(email, code.toString(), 5, TimeUnit.MINUTES);

                return R.ok("邮件发送成功");
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return R.error("邮件发送失败");
    }

    @PostMapping("/login")
    public R login(@RequestBody Map<String, Object> map, HttpSession session){
        log.info(map.toString());
        String email = (String) map.get("email");
        String code = (String) map.get("code");
//        String sessionCode = session.getAttribute(email).toString();
        String sessionCode = (String) redisTemplate.opsForValue().get(email);
        if(code != null && code.equals(sessionCode)){
            // 登录成功，看看数据库有没有这个人
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("email", email);
            User user = userService.getOne(wrapper);

            if(user == null){   // 新用户
                user = new User();
                user.setEmail(email);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());

            redisTemplate.delete(email);

            return R.ok().put("data", user);
        }
        return R.error("登陆失败");

    }

}
