package com.ovalm.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ovalm.reggie.common.BaseContext;
import com.ovalm.reggie.common.R;
import com.ovalm.reggie.entity.AddressBook;
import com.ovalm.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public R list() {
        Long userId = BaseContext.getCurrentId();
        log.info("获取address： {}", userId);

        QueryWrapper<AddressBook> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("update_time");
        List<AddressBook> list = addressBookService.list(wrapper);

        return R.ok().put("data", list);
    }

    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    public R getDefault() {
        Long userId = BaseContext.getCurrentId();
        QueryWrapper<AddressBook> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("is_default", 1);
        AddressBook addressBook = addressBookService.getOne(wrapper);
        if(addressBook == null){
            return R.error("找不到默认地址");
        }
        else{
            return R.ok().put("data", addressBook);
        }
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if(addressBook == null){
            return R.error("找不到该对象");
        }
        else{
            return R.ok().put("data", addressBook);
        }
    }

    /**
     * 设置默认地址
     */
    @PutMapping("/default")
    public R setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return R.ok();
    }

    /**
     * 新增
     */
    @PostMapping
    public R save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.ok().put("data", addressBook);
    }

    /**
     * 修改地址信息
     * @param addressBook
     * @return
     */
    @PutMapping
    public R update(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.updateById(addressBook);
        return R.ok();
    }

    /**
     * 根据id删除地址信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public R update(Long[] ids) {
        addressBookService.removeBatchByIds(Arrays.stream(ids).toList());
        return R.ok();
    }
}
