package com.example.demo.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Class.Questionnaire;
import com.example.demo.mapper.QuestionnaireMapper;
import com.example.demo.service.QuestionnaireService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class QuestionnaireServiceImpl extends ServiceImpl<QuestionnaireMapper,Questionnaire> implements QuestionnaireService {
   @Resource
   private QuestionnaireMapper questionnaireMapper;
    @Override
    public List<Questionnaire> listAll() {
        return questionnaireMapper.listAll();
    }
}


