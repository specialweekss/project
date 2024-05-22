package com.example.demo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.Class.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService extends IService<Question> {

    List<Question> listAll();
}
