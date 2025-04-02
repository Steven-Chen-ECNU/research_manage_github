package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Type;
import com.example.mapper.TypeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class TypeService {

    @Resource
    private TypeMapper typeMapper;

    public void add(Type type) {
        typeMapper.insert(type);
    }

    public void updateById(Type type) {
        typeMapper.updateById(type);
    }

    public void deleteById(Integer id) {
        typeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            typeMapper.deleteById(id);
        }
    }

    public Type selectById(Integer id) {
        return typeMapper.selectById(id);
    }

    public List<Type> selectAll(Type type) {
        return typeMapper.selectAll(type);
    }

    public PageInfo<Type> selectPage(Type type, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Type> list = typeMapper.selectAll(type);
        return PageInfo.of(list);
    }

}
