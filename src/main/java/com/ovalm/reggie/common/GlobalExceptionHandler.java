package com.ovalm.reggie.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R exceptionHandler(SQLIntegrityConstraintViolationException e){
        if(e.getMessage().contains("Duplicate entry")){
            String[] ss = e.getMessage().split(" ");
            return R.error("已存在 " + ss[2]);
        }
        return R.error("失败了");
    }

    @ExceptionHandler(CustomException.class)
    public R exceptionHandler(CustomException e){
        log.error(e.getMessage());
        return R.error(e.getMessage());
    }
}
