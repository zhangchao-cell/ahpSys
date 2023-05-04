package com.zc.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zc.demo.entity.Scheme;
import com.zc.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
