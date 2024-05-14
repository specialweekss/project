package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Class.Questionnaire_question;
import com.example.demo.mapper.Questionnaire_questionMapper;
import com.example.demo.service.Questionnaire_questionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class Questionnaire_questionServiceImpl extends ServiceImpl<Questionnaire_questionMapper, Questionnaire_question> implements Questionnaire_questionService {
    @Resource
    private Questionnaire_questionMapper questionnaire_questionMapper;

    @Override
    public List<Questionnaire_question> listAll(){
        return questionnaire_questionMapper.listAll();
    }
}
