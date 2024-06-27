package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.Class.Selection;
import com.example.demo.mapper.SelectionMapper;
import com.example.demo.service.SelectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class SelectionServiceImpl extends ServiceImpl<SelectionMapper, Selection> implements SelectionService {
    @Resource
    private SelectionMapper selectionMapper;
    @Override
    public List<Selection> listAll()
    {
        return selectionMapper.listAll();
    }

}
