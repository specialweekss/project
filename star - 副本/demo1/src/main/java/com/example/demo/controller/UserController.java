package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Class.User;
import com.example.demo.other.Result;
import com.example.demo.service.LogService;
import lombok.extern.java.Log;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    private LogService logservice;
    @CrossOrigin
    @GetMapping("/UserList")
    public Result UserList()
    {
   return Result.success(logservice.listAll(),logservice.count());
    }
    @CrossOrigin
    @GetMapping("/Register")
    public Result Register(String username,String password)
    {
        if(registercheck(username, password))
        {
            if (namecheck(username))
            {
                User user = new User();
                user.setName(username);
                user.setPassword(password);

                logservice.save(user);
                LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(User::getName,username);
                List<User> list = logservice.list(wrapper);
                return Result.success(list.get(0).getUserId());
            }
            else     return Result.fail(1,"账号已存在");
        }
        else     return Result.fail(0,"密码账号不合规");
    }
    @CrossOrigin
    @GetMapping("/LogIn")
    public Result LogIn(int userId,String password,int type)
    {
       if(idcheck(userId,password,type))
       {
           return Result.success(userId);//成功返回userid
       }
       else return Result.fail(2,"密码或账号错误");
    }
    @CrossOrigin
    @GetMapping("/getUserById")
    public Result getUserById(int userId)
    {
        return Result.success(logservice.getById(userId));
    }
    @CrossOrigin
    @PostMapping("/ModUser")//修改密码或名称
    public Result ModPassword(int userId,String newName,String newPassword)
    {

          //  if(registercheck(newName,newPassword))
           // {
                User user = logservice.getById(userId);
                user.setPassword(newPassword);
                user.setName(newName);
                logservice.updateById(user);
                return Result.success(userId);
           // }
            //else return Result.fail(0,"密码账号不合规");
    }

    @CrossOrigin
    @PostMapping("/logOut")//注销
    public Result Logout(int userId)//,String password,int type)
    {
        //if (idcheck(userId,password,type))
        //{
          return DeleteUser(userId);
       // }
        //else     return Result.fail(2,"密码或账号错误");
    }

   //登出时不需向后端传参，因此交由前端处理
   @CrossOrigin
    @GetMapping("/DeleteUser")//删除
    public Result DeleteUser(int userId)
    {
      if(logservice.removeById(userId))
        return Result.success();
      else return Result.fail(3,"删除失败");
    }

    @CrossOrigin
    @GetMapping("/ModUser")//修改密码或名称
    public Result ModPassword(int userId,String userName,String password,int type)
    {
        if (idcheck(userId,password, type))
        {
            if(registercheck(userName,password))
            {
                User user = logservice.getById(userId);
                user.setPassword(password);
                user.setName(userName);
                return Result.success(userId);
            }
           else return Result.fail(0,"密码账号不合规");
        }
        else     return Result.fail(2,"密码或账号错误");
    }

    public boolean registercheck(String name,String password)//仅检查命名规则
    {
        int flag1=0,flag2=0,flag3=0;
        if(name.length()<2||password.length()<3||name.length()>8||password.length()>10)return false;
        else
            for(int i=0;i<password.length();i++)
            {
                if(password.charAt(i)>65&&password.charAt(i)<90)flag1=1;
                if(password.charAt(i)>97&&password.charAt(i)<122)flag2=1;
                if(password.charAt(i)>123&&password.charAt(i)<152)flag3=1;
            }
        return flag1 == 1 && flag2 == 1 && flag3 == 1;
    }


    public boolean namecheck(String username)//检查名字有无使用
    {
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getName,username);
        return logservice.count(wrapper)>0;
    }

    public boolean idcheck(int userId,String password,int type)//检查id和密码
    {
        User user=logservice.getById(userId);
        if (user == null)return false;
        else return Objects.equals(user.getPassword(), password) && user.getType() == type;
    }


}
