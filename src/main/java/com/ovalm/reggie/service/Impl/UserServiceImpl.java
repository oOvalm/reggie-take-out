package com.ovalm.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ovalm.reggie.entity.User;
import com.ovalm.reggie.mapper.UserBaseMapper;
import com.ovalm.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserBaseMapper, User> implements UserService {
}
