package com.ovalm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ovalm.reggie.entity.AddressBook;

public interface AddressBookService extends IService<AddressBook> {
    void setDefault(AddressBook addressBook);
}
