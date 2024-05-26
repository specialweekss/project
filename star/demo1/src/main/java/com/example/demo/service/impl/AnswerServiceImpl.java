package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Class.Answer;
import com.example.demo.mapper.AnswerMapper;
import com.example.demo.service.AnswerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {
    @Resource
    private AnswerMapper answerMapper;
    @Override
    public List<Answer> listAll() {return answerMapper.listAll();}
}
