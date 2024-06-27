package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.Class.Answer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnswerService extends IService<Answer> {
    List<Answer> listAll();
}
