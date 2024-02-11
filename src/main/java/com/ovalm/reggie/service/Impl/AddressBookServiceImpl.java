package com.ovalm.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ovalm.reggie.common.BaseContext;
import com.ovalm.reggie.entity.AddressBook;
import com.ovalm.reggie.mapper.AddressBookBaseMapper;
import com.ovalm.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookBaseMapper, AddressBook> implements AddressBookService {

    @Transactional
    @Override
    public void setDefault(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        UpdateWrapper<AddressBook> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", userId).set("is_default", 0);
        this.update(wrapper);

        addressBook.setIsDefault(1);
        this.updateById(addressBook);
    }
}
