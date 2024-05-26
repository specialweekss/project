package com.example.demo.Class;

import lombok.Data;

import java.util.Date;

@Data//类
public class Questionnaire {
    String theme;
    int id;
    int creatorId;
    int state;
 Date releaseTime;
    Date endTime;
    int questionNum;
    int commitNum;
}
