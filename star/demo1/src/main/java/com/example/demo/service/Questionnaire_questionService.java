package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.Class.Questionnaire_question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface Questionnaire_questionService extends IService<Questionnaire_question> {
    List<Questionnaire_question> listAll();
}
