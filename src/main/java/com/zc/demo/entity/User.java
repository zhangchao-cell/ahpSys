package com.zc.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    Integer id;
    String username;
    String password;
    String email;
    String phone;

}
