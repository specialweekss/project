package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Class.Question;
import com.example.demo.Class.Questionnaire;
import com.example.demo.other.Result;
import com.example.demo.service.QuestionService;
import com.example.demo.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController//当成main
public class Controller {
    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private QuestionService questionService;

    @CrossOrigin
    @GetMapping("/List")
    public Result getQuestionnaire() {
        return Result.success(questionnaireService.listAll(),questionnaireService.count());
    }
    @CrossOrigin
    @GetMapping("/ListByUser")
    public Result getQuestionnaireByUser(int userId) {
        LambdaQueryWrapper<Questionnaire> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Questionnaire::getCreatorId,userId);
        List<Questionnaire> questionnaireList = questionnaireService.list(queryWrapper);
        if(!questionnaireList.isEmpty())
            return Result.success(questionnaireList,questionnaireList.size());
        else
            return Result.fail();
    }

    @GetMapping("/ListQuestion")
    public Result getQuestion() {
        return Result.success(questionService.listAll(),questionService.count());
    }
    @GetMapping("/ListQuestionInIt")
    public Result getQuestionInIt(int id) {
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<Question>();
        wrapper.eq(Question::getQuestionnaireId,id);
        List<Question> list = questionService.list(wrapper);
        if(!list.isEmpty())
        return Result.success(list, list.size());
        else
            return Result.fail();
    }
@CrossOrigin
    @PostMapping("/save")
    public Result save(@RequestBody Questionnaire questionnaire) {
        if(questionnaireService.save(questionnaire))
            return Result.success(questionnaire);
        else
            return Result.fail();
    }
    @CrossOrigin
    @PostMapping("/saveQuestion")
    public Result saveQuestion(int id,@RequestBody Question question) {
        Questionnaire questionnaire = questionnaireService.getById(id);
        question.setNumber(questionnaire.getQuestionNum()+1);
        questionnaire.setQuestionNum(questionnaire.getQuestionNum()+1);
        question.setQuestionnaireId(id);
        if(questionService.save(question)&&questionnaireService.updateById(questionnaire)) {
                 return Result.success(questionnaire);
        }
        else
            return Result.fail();
    }


    @PostMapping("/mod")
    public Result mod(int id,String theme) {
        Questionnaire questionnaire = questionnaireService.getById(id);
        if(questionnaire != null) {
            questionnaire.setTheme(theme);
            questionnaireService.updateById(questionnaire);
            return Result.success(questionnaire);
        }
        else
            return Result.fail("用户不存在！");
    }
    @PostMapping("/modQuestion")
    public Result modQuestion(int questionId,@RequestBody Question question) {
        Question oldQuestion = questionService.getById(questionId);
        oldQuestion.setNecessary(question.getNecessary());
        oldQuestion.setTitle(question.getTitle());
        oldQuestion.setType(question.getType());
        if(questionService.updateById(oldQuestion))
                return Result.success(oldQuestion);
        else
            return Result.fail();
    }
   @CrossOrigin
    @PostMapping("/delete")
    public Result delete( int id) {
        if(questionnaireService.removeById(id))
            return Result.success(id);
        else
            return Result.fail();

    }
    @PostMapping("/deleteQuestion")
    public Result deleteQuestion(int questionId) {
        int number=questionService.getById(questionId).getNumber();
        int questionnaireId=questionService.getById(questionId).getQuestionnaireId();
        if(questionService.removeById( questionId) ){
            Questionnaire questionnaire=questionnaireService.getById(questionnaireId);
            questionnaire.setQuestionNum(questionnaire.getQuestionNum()-1);
            questionnaireService.updateById(questionnaire);
            LambdaQueryWrapper<Question> deleteWrapper = new LambdaQueryWrapper<Question>();
            deleteWrapper.eq(Question::getQuestionnaireId,questionnaireId).gt(Question::getNumber,number);
           List<Question> list = questionService.list(deleteWrapper);
           list.forEach(question->{
               question.setNumber(question.getNumber()-1);
               questionService.updateById(question);
           });
                   return Result.success(questionId);
        }
        else
            return Result.fail("删除问题失败！");
    }
@CrossOrigin
    @PostMapping("/publish")
    public Result publish(@RequestBody Questionnaire pub) {
       Questionnaire questionnaire = questionnaireService.getById(pub.getId());
       if(questionnaire.getState()==0) {
           questionnaire.setState(1);
           questionnaire.setReleaseTime(pub.getReleaseTime());
           questionnaire.setEndTime(pub.getEndTime());
           if(questionnaireService.updateById(questionnaire))
           return Result.success(questionnaire);
           else
               return Result.fail();
       }
       else if(questionnaire.getState()==1) {
           return Result.fail(1,"问卷已发布,返回错误1");
        }
       else
       {
           return Result.fail(2,"问卷已截止，如果要重新发布请选择rePublish,返回错误2");
       }
    }
    @CrossOrigin
    @PostMapping("/rePublish")
    public Result rePublish(@RequestBody Questionnaire time) {
        Questionnaire questionnaire = questionnaireService.getById(time.getId());
            questionnaire.setState(1);
        questionnaire.setReleaseTime(time.getReleaseTime());
        questionnaire.setEndTime(time.getEndTime());
        if(questionnaireService.updateById(questionnaire))
            return Result.success(questionnaire);
        else
            return Result.fail();
    }
    @CrossOrigin
    @PostMapping("/end")
    public Result end(int id) {
        Questionnaire questionnaire = questionnaireService.getById(id);
        if(questionnaire.getState()==0) {
            return Result.fail(0,"问卷未发布，返回数据0");
        }
        else if(questionnaire.getState()==2) {
            return Result.fail(2,"问卷已截止，返回数据2");
        }
        questionnaire.setState(2);
        if(questionnaireService.updateById(questionnaire))
            return Result.success(questionnaire);
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

    @PostMapping("/listP")
    public Result listP(@RequestBody Questionnaire questionnaire)
    {
        LambdaQueryWrapper<Questionnaire> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Questionnaire::getTheme, questionnaire.getTheme());
        return Result.success(questionnaireService.list(wrapper),questionnaireService.count(wrapper));
    }
    @PostMapping("/ListQuestionP")
    public Result listPQuestion(@RequestBody Question question)
    {
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Question::getTitle, question.getTitle());
        return Result.success(questionService.list(wrapper),questionService.count(wrapper));
    }
}
