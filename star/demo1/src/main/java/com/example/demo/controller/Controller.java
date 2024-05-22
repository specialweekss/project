package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Class.Question;
import com.example.demo.Class.Questionnaire;
import com.example.demo.Class.Questionnaire_question;
import com.example.demo.other.Result;
import com.example.demo.service.QuestionService;
import com.example.demo.service.QuestionnaireService;
import com.example.demo.service.Questionnaire_questionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController//当成main
public class Controller {
    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private Questionnaire_questionService questionnaire_questionService;

    @CrossOrigin
    @GetMapping("/List")
    public Result getQuestionnaire() {
        return Result.success(questionnaireService.listAll(),questionnaireService.count());
    }
    @GetMapping("/ListQuestion")
    public Result getQuestion() {
        return Result.success(questionService.listAll(),questionService.count());
    }
    @GetMapping("/ListQuestionnaire_question")
    public Result getQuestionnaire_question() {
        return Result.success(questionnaire_questionService.listAll(),questionService.count());
    }
    @GetMapping("/ListQuestionInIt")
    public Result getQuestionInIt(int questionnaireId) {
        LambdaQueryWrapper<Questionnaire_question> wrapper = new LambdaQueryWrapper<Questionnaire_question>();
        wrapper.eq(Questionnaire_question::getQnaireId,questionnaireId);
        List<Questionnaire_question> list = questionnaire_questionService.list(wrapper);
        List<Question> questions=new ArrayList<Question>();
        list.forEach(questionnaire_question -> {
            Question question = questionService.getById(questionnaire_question.getQuestionId());
           questions.add(question);
        });
        return Result.success(questions, questions.size());
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
    public Result saveQuestion(int questionnaireId,@RequestBody Question question) {
        if(questionService.save(question)) {
            Questionnaire_question questionnaire_question = new Questionnaire_question();
            questionnaire_question.setQnaireId(questionnaireId);
            questionnaire_question.setQuestionId(question.getQuestionId());
         if(questionnaire_questionService.save(questionnaire_question))
             return Result.success(question);
         else
             return Result.fail();
        }
        else
            return Result.fail();
    }


    @PostMapping("/mod")
    public Result mod(@RequestBody Questionnaire questionnaire) {
        if(questionnaireService.updateById(questionnaire))
            return Result.success(questionnaire);
        else
            return Result.fail();
    }
    @PostMapping("/modQuestion")
    public Result modQuestion(@RequestBody Question question) {
        if(questionService.updateById(question))
                return Result.success(question);
        else
            return Result.fail();
    }

//    @PostMapping("saveOrMod")
//    public Result saveOrMod(@RequestBody Questionnaire questionnaire) {
//        if(questionnaireService.saveOrUpdate(questionnaire))
//            return Result.success(questionnaire);
//        else
//            return Result.fail();
//    }

    @GetMapping("/delete")
    public Result delete( int id) {
        if(questionnaireService.removeById(id))
            return Result.success(id);
        else
            return Result.fail();

    }
    @PostMapping("/deleteQuestion")
    public Result deleteQuestion(int questionnaireId,int questionId) {
        if(questionService.removeById( questionId) ){
            LambdaQueryWrapper<Questionnaire_question> deleteWrapper = new LambdaQueryWrapper<Questionnaire_question>();
            deleteWrapper.eq(Questionnaire_question::getQnaireId,questionnaireId).eq(Questionnaire_question::getQuestionId,questionId);
            if(questionnaire_questionService.remove(deleteWrapper)) {
                LambdaQueryWrapper<Questionnaire_question> wrapper = new LambdaQueryWrapper<Questionnaire_question>();
                wrapper.eq(Questionnaire_question::getQnaireId,questionnaireId);
                List<Questionnaire_question> list = questionnaire_questionService.list(wrapper);
                list.forEach(questionnaire_question -> {
                    Question question = questionService.getById(questionnaire_question.getQuestionId());
                    question.setNumber(question.getNumber() - 1);
                    questionService.updateById(question);
                });
                return Result.success();
            }
            else
            {
                Question question = new Question();
                question.setQuestionId(questionId);
                saveQuestion(questionnaireId,question);
                return Result.fail("删除问题成功，删除问卷联系失败！\n请根据上文判断空白问题是否创建成功，创建成功后重新执行删除操作\n如创建失败请手动从数据库删除联系" );
            }
        }
        else
            return Result.fail("删除问题失败！");
    }

    @PostMapping("/publish")
    public Result publish(int id) {
       Questionnaire questionnaire = questionnaireService.getById(id);
       if(questionnaire.getState()==0) {
           questionnaire.setState(1);
           return Result.success(questionnaire);
       }
       else if(questionnaire.getState()==1) {
           return Result.fail(1,"问卷已发布,返回错误1");
        }
       else
       {
           return Result.fail(2,"问卷已截止，如果要重新发布请选择rePublish,返回错误2");
       }
    }
    @PostMapping("/rePublish")
    public Result rePublish(int id) {
        Questionnaire questionnaire = questionnaireService.getById(id);
            questionnaire.setState(1);
            return Result.success(questionnaire);
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
