package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Class.Answer;
import com.example.demo.Class.Questionnaire;
import com.example.demo.other.Result;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Class.Record;

import java.util.Date;
import java.util.List;

@RestController
public class RecordController {
    @Autowired
    LogService logService;
    @Autowired
    RecordService recordService;
    @Autowired
    AnswerService answerService;
    @Autowired
    QuestionService questionService;
    @Autowired
    private QuestionnaireService questionnaireService;

    @CrossOrigin
    @GetMapping("/ListRecord")
    public Result listRecord() {
        return Result.success(recordService.listAll(),recordService.count());
    }
    @CrossOrigin
    @GetMapping("/ListRecordInQuestionnaire")
    public Result listRecordInIt(int questionnaireId) {
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getQuestionnaireId, questionnaireId);
        if(recordService.count(queryWrapper) > 0) {
            return Result.success(recordService.list(queryWrapper));
        }
        else
            return Result.fail();
    }
    @CrossOrigin
    @GetMapping("/ListRecordByUser")
    public Result listRecordByUser(int userId) {
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getUserId, userId).eq(Record::getIsDeleted,0);
        if(recordService.count(queryWrapper) > 0) {
            return Result.success(recordService.list(queryWrapper));
        }
        else
            return Result.fail();
    }
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
    @GetMapping("/ListAnswerByUser")
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
    @PostMapping("/saveRecord")
    public Result saveRecord(int userId, int questionnaireId) {
        Record record=new Record();
        record.setUserId(userId);
        record.setQuestionnaireId(questionnaireId);
        record.setIsDeleted(0);
        Questionnaire questionnaire=questionnaireService.getById(questionnaireId);
        record.setTheme(questionnaire.getTheme());
        Date date=new Date();
        record.setRecordTime(date);
        if(recordService.save(record))
            return Result.success(record);
        else
            return Result.fail();
    }
    @CrossOrigin
    @PostMapping("/deleteRecord")
    public Result deleteRecord(int questionnaireId, int userId) {
        LambdaQueryWrapper<Record> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Record::getQuestionnaireId, questionnaireId).eq(Record::getUserId, userId);
       List<Record> records=recordService.list(wrapper);
       Record record=records.get(0);
       record.setIsDeleted(1);
       if(recordService.updateById(record))
           return Result.success(record);
       else
           return Result.fail();
    }
}
