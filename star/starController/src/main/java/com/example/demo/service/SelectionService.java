package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.Class.Selection;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SelectionService extends IService<Selection> {
    List<Selection> listAll();
}
