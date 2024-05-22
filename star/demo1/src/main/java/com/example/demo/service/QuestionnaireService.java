package com.example.demo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.Class.Questionnaire;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionnaireService extends IService<Questionnaire> {

    List<Questionnaire> listAll();
}
