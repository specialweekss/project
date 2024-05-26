package com.example.demo.Class;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Answer {
    @TableId
    int answerId;
    int questionId;
    int userId;
    int type;
    String Answer ;
}
