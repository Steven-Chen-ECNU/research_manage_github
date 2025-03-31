package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Process;
import com.example.mapper.ProcessMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class ProcessService {

    @Resource
    private ProcessMapper processMapper;

    public void add(Process process) {
        Account currentUser = TokenUtils.getCurrentUser();
        process.setTeacherId(currentUser.getId());
        processMapper.insert(process);
    }

    public void updateById(Process process) {
        processMapper.updateById(process);
    }

    public void deleteById(Integer id) {
        processMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            processMapper.deleteById(id);
        }
    }

    public Process selectById(Integer id) {
        return processMapper.selectById(id);
    }

    public List<Process> selectAll(Process process) {
        return processMapper.selectAll(process);
    }

    public PageInfo<Process> selectPage(Process process, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.TEACHER.name().equals(currentUser.getRole())) {
            process.setTeacherId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Process> list = processMapper.selectAll(process);
        return PageInfo.of(list);
    }

}
