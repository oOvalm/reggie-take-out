package com.ovalm.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.ovalm.reggie.common.BaseContext;
import com.ovalm.reggie.common.R;
import com.ovalm.reggie.entity.Employee;
import com.ovalm.reggie.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R login(HttpServletRequest request, @RequestBody Employee employee){
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        String psw = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        wrapper.eq(Employee::getUsername, employee.getUsername());
        Employee one = employeeService.getOne(wrapper);
        if(one == null){
            return R.error("用户不存在");
        }
        if(!one.getPassword().equals(psw)){
            return R.error("密码错误");
        }
        if(one.getStatus() == 0){
            return R.error("账号已禁用");
        }
        request.getSession().setAttribute("employee", one.getId());
        return R.ok().put("data", one);
    }


    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.ok();
    }

    /**
     * 添加员工
     */
    @PostMapping
    public R save(@RequestBody Employee employee){
        log.info("员工：{}", employee);
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);
        return R.ok();
    }

    /**
     * 员工数据分页查询
     */
    @GetMapping("/page")
    public R listPage(int page, int pageSize, String name){

        Page pageInfo = new Page(page, pageSize);

        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        if(!StringUtils.isNullOrEmpty(name)) {
            wrapper.like("name", name);
        }
        wrapper.orderByDesc("update_time");

        employeeService.page(pageInfo, wrapper);
        return R.ok().put("data", pageInfo);
    }


    /**
     * 更新员工信息
     */
    @PutMapping
    public R update(@RequestBody Employee employee){
        log.info("改变员工状态 {}", employee);
        long id = Thread.currentThread().getId();
        log.info("线程Id: {}", id);
        employeeService.updateById(employee);
        return R.ok();
    }

    /**
     * 查询员工信息
     * @param request
     * @param emId
     * @return
     */
    @GetMapping("/{emId}")
    public R infoById(@PathVariable("emId") Long emId){
        Employee emp = employeeService.getById(emId);
        if(emp == null){
            return R.error("查不到员工信息");
        }
        return R.ok().put("data", emp);

    }
}
