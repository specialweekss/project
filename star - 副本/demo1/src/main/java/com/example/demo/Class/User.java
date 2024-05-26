package com.example.demo.Class;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User
{
    @TableId
    int userId;
    int type;
    String name;
    String password;


}
