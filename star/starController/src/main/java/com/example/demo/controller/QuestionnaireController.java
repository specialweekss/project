package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Class.*;
import com.example.demo.Class.Record;
import com.example.demo.other.Result;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;


@RestController//当成main
public class QuestionnaireController {
    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private RecordService recordService;
@Autowired
private AnswerService answerService;
@Autowired
private SelectionService selectionService;
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
@CrossOrigin
    @GetMapping("/ListQuestion")
    public Result getQuestion() {
        return Result.success(questionService.listAll(),questionService.count());
    }
    @CrossOrigin
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
    @GetMapping("/ListSelection")
    public Result ListSelection() {
        return Result.success(selectionService.listAll(),selectionService.count());
    }
    @CrossOrigin
    @GetMapping("ListSelectionInIt")
    public Result ListSelectionInIt(int questionId) {
        LambdaQueryWrapper<Selection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Selection::getQuestionId,questionId);
        List<Selection> list = selectionService.list(wrapper);
        if(!list.isEmpty())
            return Result.success(list,list.size());
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
        if(question.getTitle().isBlank() || question.getTitle().isEmpty())
        {
            return Result.fail(0,"问题无标题");
        }
        Questionnaire questionnaire = questionnaireService.getById(id);
        question.setNumber(questionnaire.getQuestionNum()+1);
        questionnaire.setQuestionNum(questionnaire.getQuestionNum()+1);
        question.setQuestionnaireId(id);
        questionService.save(question);
        if(question.getType()==1||question.getType()==0)
        {
            Selection selection = new Selection();
            selection.setContent("选项");
            LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Question::getQuestionnaireId,id).eq(Question::getNumber,question.getNumber());
            List<Question> list = questionService.list(wrapper);
            int questionId=list.get(0).getQuestionId();
            saveSelection(questionId,selection);
            saveSelection(questionId,selection);
        }
        if(questionnaireService.updateById(questionnaire)) {
                 return Result.success(question);
        }
        else
            return Result.fail();
    }
    @CrossOrigin
    @PostMapping("/saveSelection")
    public Result saveSelection(int questionId,@RequestBody Selection selection) {
       LambdaQueryWrapper<Selection> wrapper = new LambdaQueryWrapper<>();
       wrapper.eq(Selection::getQuestionId,questionId);
       int count = selectionService.count(wrapper);
       selection.setQuestionId(questionId);
       selection.setPosition(count+1);
       Question question = questionService.getById(questionId);
       question.setSelectionNum(question.getSelectionNum()+1);
       if(selectionService.save(selection)&&questionService.updateById(question)) {
           return Result.success(selection);
       }
       else
           return Result.fail();
    }
    @CrossOrigin
@GetMapping("/getById")
public Result getById(int id) {
        Questionnaire questionnaire=questionnaireService.getById(id);

        if(questionnaire!=null)
            return Result.success(questionnaire);
        else
            return Result.fail();
}
    @CrossOrigin
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
    @CrossOrigin
    @PostMapping("/modQuestion")
    public Result modQuestion(int questionId,@RequestBody Question question) {
        if(question.getTitle().isBlank() || question.getTitle().isEmpty())
        {
            return Result.fail(0,"问题无标题");
        }
        Question oldQuestion = questionService.getById(questionId);
        oldQuestion.setNecessary(question.getNecessary());
        oldQuestion.setTitle(question.getTitle());
        oldQuestion.setType(question.getType());
        if(questionService.updateById(oldQuestion)) {
            //标记答案无效
            inValidAnswer(questionId);
            return Result.success(oldQuestion);
        }
        else
            return Result.fail();
    }

    public void inValidAnswer(int questionId) {
        LambdaQueryWrapper<Answer> answerWrapper = new LambdaQueryWrapper<>();
        answerWrapper.eq(Answer::getQuestionId,questionId).eq(Answer::getIsValid,1);
        List<Answer> answerList = answerService.list(answerWrapper);
        if(!answerList.isEmpty()) {
            answerList.forEach(answer -> {
                answer.setIsValid(0);
                answerService.updateById(answer);
            });
        }
    }

    @CrossOrigin
    @PostMapping("/modSelection")
    public Result modSelection(int selectionId,String content) {
        if(content.isBlank()|| content.isEmpty())
        {
            return Result.fail(0,"选项无标题");
        }
        Selection oldSelection = selectionService.getById(selectionId);
        oldSelection.setContent(content);
        if(selectionService.updateById(oldSelection))
        {
            inValidAnswer(oldSelection.getQuestionId());
            return Result.success(oldSelection);
        }
        else
            return Result.fail();
    }
   @CrossOrigin
    @PostMapping("/delete")
    public Result delete( int id) {
        if(questionnaireService.removeById(id)) {
            //删除问题
            LambdaQueryWrapper<Question> questionWrapper = new LambdaQueryWrapper<>();
            questionWrapper.eq(Question::getQuestionnaireId,id);
            List<Question> list = questionService.list(questionWrapper);
            if(!list.isEmpty()) {
                list.forEach(question -> {
                    deleteQuestion(question.getQuestionId());
                });
            }
            LambdaQueryWrapper<Record> recordWrapper = new LambdaQueryWrapper<>();

            return Result.success(id);
        }
        else
            return Result.fail();

    } @CrossOrigin
    @PostMapping("/deleteQuestion")
    public Result deleteQuestion(int questionId) {
        int number=questionService.getById(questionId).getNumber();
        int questionnaireId=questionService.getById(questionId).getQuestionnaireId();
        if(questionService.removeById( questionId) ){

            Questionnaire questionnaire=questionnaireService.getById(questionnaireId);
            if(questionnaire!=null) {
                //问卷问题-1
                questionnaire.setQuestionNum(questionnaire.getQuestionNum() - 1);
                questionnaireService.updateById(questionnaire);
                //问卷其他问题改变序号
                LambdaQueryWrapper<Question> deleteWrapper = new LambdaQueryWrapper<Question>();
                deleteWrapper.eq(Question::getQuestionnaireId, questionnaireId).gt(Question::getNumber, number);
                List<Question> list = questionService.list(deleteWrapper);
                list.forEach(question -> {
                    question.setNumber(question.getNumber() - 1);
                    questionService.updateById(question);
                });
            }
            //标记答案无效
            inValidAnswer(questionId);
            //删除所有选项
           LambdaQueryWrapper<Selection> selectionWrapper = new LambdaQueryWrapper<>();
           selectionWrapper.eq(Selection::getQuestionId,questionId);
           List<Selection> selectionList = selectionService.list(selectionWrapper);
           if(!selectionList.isEmpty()) {
               selectionList.forEach(selection -> {
                   selectionService.removeById(selection.getSelectionId());
               });
           }


                   return Result.success(questionId);
        }
        else
            return Result.fail("删除问题失败！");
    }
    @CrossOrigin
    @PostMapping("/deleteSelection")
    public Result deleteSelection(int selectionId) {

        Selection oldSelection = selectionService.getById(selectionId);
        Question question = questionService.getById(oldSelection.getQuestionId());
        if(question.getSelectionNum()<=2)
        {
            return Result.fail(0,"选项只剩两个");
        }
        if(selectionService.removeById(selectionId))
        {
            LambdaQueryWrapper<Selection> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(Selection::getQuestionId,oldSelection.getQuestionId()).gt(Selection::getSelectionId,selectionId);
            List<Selection> list = selectionService.list(deleteWrapper);
            list.forEach(selection->{
                selection.setPosition(selection.getPosition()-1);
                selectionService.updateById(selection);
            });
            question.setSelectionNum(question.getSelectionNum()-1);
            if(questionService.updateById(question)) {

                    inValidAnswer(question.getQuestionId());
                return Result.success(question);
            }
            else
                return Result.fail(2,"更新问题失败，请删除问题");
        }
        else
            return Result.fail(1,"删除选项失败");
    }

@CrossOrigin
    @PostMapping("/publish")
    public Result publish(@RequestBody Questionnaire pub) {
       Questionnaire questionnaire = questionnaireService.getById(pub.getId());
//       if(questionnaire.getState()==0) {
           questionnaire.setState(1);
           questionnaire.setReleaseTime(pub.getReleaseTime());
           questionnaire.setEndTime(pub.getEndTime());
           if(questionnaireService.updateById(questionnaire))
           return Result.success(questionnaire);
           else
               return Result.fail();
       }
//       else if(questionnaire.getState()==1) {
//           return Result.fail(1,"问卷已发布,返回错误1");
//        }
//       else
//       {
//           return Result.fail(2,"问卷已截止，如果要重新发布请选择rePublish,返回错误2");
//       }
//    }
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
    @GetMapping("getIp")
    public String getIp() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }
}
