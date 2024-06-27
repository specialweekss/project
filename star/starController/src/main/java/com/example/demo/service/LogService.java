package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.Class.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LogService extends IService<User>
{
    List<User> listAll();
}
