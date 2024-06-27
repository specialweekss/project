package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.Class.Selection;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SelectionMapper extends BaseMapper<Selection> {
 List<Selection> listAll() ;
}
