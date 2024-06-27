package com.example.demo.Class;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Selection {
    @TableId
    int selectionId;
    int questionId;
    int position;
    String content;
}
