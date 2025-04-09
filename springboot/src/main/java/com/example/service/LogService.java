package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Log;
import com.example.mapper.LogMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class LogService {

    @Resource
    private LogMapper logMapper;

    public void add(Log log) {
        logMapper.insert(log);
    }

    public void deleteById(Integer id) {
        logMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            logMapper.deleteById(id);
        }
    }

    public PageInfo<Log> selectPage(Log log, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Log> list = logMapper.selectAll(log);
        return PageInfo.of(list);
    }

}
