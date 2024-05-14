package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.Class.Questionnaire_question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Questionnaire_questionMapper extends BaseMapper<Questionnaire_question> {
    List<Questionnaire_question> listAll();
}
