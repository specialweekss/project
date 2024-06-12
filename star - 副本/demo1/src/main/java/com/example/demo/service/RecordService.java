package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.Class.Record;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordService extends IService<Record> {
    List<Record> listAll();
}
