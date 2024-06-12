package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Class.Answer;
import com.example.demo.Class.Question;
import com.example.demo.Class.Questionnaire;
import com.example.demo.Class.Record;
import com.example.demo.controller.RecordController;
import com.example.demo.other.Result;
import com.example.demo.service.AnswerService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.QuestionnaireService;
import com.example.demo.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
public class fillController {
    @Autowired
    private AnswerService answerService;
    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    RecordController recordController;
    @CrossOrigin
    @PostMapping("/fill")
    public Result fill(int userId,@RequestBody Answer answer) {
        answer.setType(questionService.getById(answer.getQuestionId()).getType());
        answer.setUserId(userId);
        if(answerService.save(answer))
        {
            Question question=questionService.getById(answer.getQuestionId());
            question.setAnswerNum(question.getAnswerNum()+1);
            if(questionService.updateById(question))
            return Result.success(answer);
            else
                return Result.fail();
        }
        else
            return Result.fail();
    }
    @CrossOrigin
    @PostMapping("/commit")
    public Result commit(int id,int userId,@RequestBody List<Answer> list) {
        list.forEach(answer -> {
            fill(userId,answer);
        });
        Questionnaire questionnaire=questionnaireService.getById(id);
        questionnaire.setCommitNum(questionnaire.getCommitNum()+1);
        recordController.saveRecord(userId,id);
        if(questionnaireService.updateById(questionnaire))
        return Result.success(list);
        else
            return Result.fail();
    }
    @CrossOrigin
    @GetMapping("/checkState")
    public Result checkState(int   id) {
        Questionnaire questionnaire=questionnaireService.getById(id);
        if(questionnaire.getState()==1) {
            Date currentDate=new Date();
            if(currentDate.before(questionnaire.getEndTime())) {
                return Result.success(questionnaire);
            }
            else {
                questionnaire.setState(2);
                questionnaireService.updateById(questionnaire);
                return Result.fail(2,"问卷已截止，返回数据2");
            }
        }
        else if(questionnaire.getState()==2)
            return Result.fail(2,"问卷已截止，返回数据2");
        else
            return Result.fail(0,"问卷未发布，返回数据0");
    }
}
