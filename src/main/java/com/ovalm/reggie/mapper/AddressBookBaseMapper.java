package com.ovalm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ovalm.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookBaseMapper extends BaseMapper<AddressBook> {
}
