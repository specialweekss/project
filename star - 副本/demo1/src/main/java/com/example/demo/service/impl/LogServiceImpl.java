package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Class.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LogServiceImpl extends ServiceImpl<UserMapper, User> implements LogService
{
    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> listAll()
    {
    return userMapper.listAll();
    };

}
