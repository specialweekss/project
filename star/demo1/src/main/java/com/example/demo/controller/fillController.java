package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Class.Answer;
import com.example.demo.Class.Question;
import com.example.demo.Class.Questionnaire;
import com.example.demo.other.Result;
import com.example.demo.service.AnswerService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class fillController {
    @Autowired
    private AnswerService answerService;
    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private QuestionService questionService;
    @CrossOrigin
    @GetMapping("/ListAnswer")
    public Result getAnswer() {
        return Result.success(answerService.listAll(),answerService.count());
    }
    @CrossOrigin
    @GetMapping("/ListAnswerInIt")
    public Result getAnswerInIt(int questionId) {
        LambdaQueryWrapper<Answer> wrapper = new LambdaQueryWrapper<Answer>();
        wrapper.eq(Answer::getQuestionId, questionId);
        List<Answer> answers=answerService.list(wrapper);
        return Result.success(answers, answers.size());
    }
    @CrossOrigin
    @GetMapping("ListAnswerByUser")
    public Result getAnswerByUser(int userId,int questionId) {
        LambdaQueryWrapper<Answer> wrapper=new LambdaQueryWrapper<Answer>();
        wrapper.eq(Answer::getUserId, userId).eq(Answer::getQuestionId, questionId);
        List<Answer> answers=answerService.list(wrapper);
        if(!answers.isEmpty())
                return Result.success(answers, answers.size());
        else
            return Result.fail();
    }
    @CrossOrigin
    @PostMapping("/fill")
    public Result fill(@RequestBody Answer answer) {
        answer.setType(questionService.getById(answer.getQuestionId()).getType());
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
    public Result commit(int id,@RequestBody List<Answer> list) {
        list.forEach(this::fill);
        Questionnaire questionnaire=questionnaireService.getById(id);
        questionnaire.setCommitNum(questionnaire.getCommitNum()+1);
        if(questionnaireService.updateById(questionnaire))
        return Result.success();
        else
            return Result.fail();
    }

}
