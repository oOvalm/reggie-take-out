package com.ovalm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ovalm.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeBaseMapper extends BaseMapper<Employee> {
}
