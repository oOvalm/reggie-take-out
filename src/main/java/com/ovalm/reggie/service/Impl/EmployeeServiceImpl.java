package com.ovalm.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ovalm.reggie.controller.EmployeeController;
import com.ovalm.reggie.entity.Employee;
import com.ovalm.reggie.mapper.EmployeeBaseMapper;
import com.ovalm.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeBaseMapper, Employee> implements EmployeeService {
}
