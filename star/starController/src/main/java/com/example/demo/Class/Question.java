package com.example.demo.Class;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Question {
    @TableId
    int questionId ;
    int number;
    String title ;
    int necessary;
    int type;
    int questionnaireId;
    int answerNum;
    int selectionNum;
}
