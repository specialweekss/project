package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Class.Answer;
import com.example.demo.other.Result;
import com.example.demo.service.AnswerService;
import com.example.demo.service.LogService;
import com.example.demo.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
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
        Date date=new Date();
        record.setRecordTime(date);
        if(recordService.save(record))
            return Result.success(record);
        else
            return Result.fail();
    }
    @CrossOrigin
    @PostMapping("/deleteRecord")
    public Result deleteRecord(int recordId) {
       Record record= recordService.getById(recordId);
       record.setIsDeleted(1);
       if(recordService.updateById(record))
           return Result.success(record);
       else
           return Result.fail();
    }
}
