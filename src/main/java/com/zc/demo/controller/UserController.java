package com.zc.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zc.demo.common.R;
import com.zc.demo.entity.User;
import com.zc.demo.mapper.UserMapper;
import com.zc.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    //方案层
    @GetMapping
    public List<User> selectAllUsers(){
        return userService.list();
    }

    @PostMapping("/save")
    public boolean saveUser(@RequestBody User user){
        return userService.save(user);
    }

    @PostMapping("/logon")
    public R Logon(@RequestBody User user){
        String password = user.getPassword();

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
        User emp = userService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        return R.success(emp);

    }


    @PostMapping("/register")
    public R Register(@RequestBody User user){
        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
        User emp = userService.getOne(queryWrapper);

        //3、如果查询到则返回登录失败结果
        if(emp != null){
            return R.error("用户名已存在");
        }

        userService.save(user);

        return R.success(user);

    }

    @PostMapping("/update")
    public boolean updateUser(@RequestBody User user){
        return userService.updateById(user);
    }

}
