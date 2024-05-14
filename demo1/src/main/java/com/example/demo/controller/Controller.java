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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController//当成main
public class Controller {
    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private Questionnaire_questionService questionnaire_questionService;


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

    @PostMapping("/save")
    public Result save(@RequestBody Questionnaire questionnaire) {
        if(questionnaireService.save(questionnaire))
            return Result.success(questionnaire);
        else
            return Result.fail();
    }
    @PostMapping("/saveQuestion")
    public Result saveQuestion(int questionnaireId,@RequestBody Question question) {
        if(questionService.save(question)) {
            Questionnaire_question questionnaire_question = new Questionnaire_question();
            questionnaire_question.setQuestionId(questionnaireId);
            questionnaire_question.setQnaireId(question.getQuestionId());
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
    @PostMapping("saveOrMod")
    public Result saveOrMod(@RequestBody Questionnaire questionnaire) {
        if(questionnaireService.saveOrUpdate(questionnaire))
            return Result.success(questionnaire);
        else
            return Result.fail();
    }

    @GetMapping("/delete")
    public Result delete( int id) {
        if(questionnaireService.removeById(id))
            return Result.success(id);
        else
            return Result.fail();

    }

    @PostMapping("/listP")
    public Result listP(@RequestBody Questionnaire questionnaire)
    {
        LambdaQueryWrapper<Questionnaire> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Questionnaire::getTheme, questionnaire.getTheme());
        return Result.success(questionnaireService.list(wrapper),questionnaireService.count(wrapper));
    }

}
