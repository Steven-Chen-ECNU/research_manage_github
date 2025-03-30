package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Project;
import com.example.mapper.ProjectMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 科研项目业务层方法
 */
@Service
public class ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    public void add(Project project) {
        project.setCode(DateUtil.parse(DateUtil.now()).toString("yyyyMMddHHmmss"));
        Account currentUser = TokenUtils.getCurrentUser();
        project.setTeacherId(currentUser.getId());
        project.setStatus("待审核");
        projectMapper.insert(project);
    }

    public void updateById(Project project) {
        projectMapper.updateById(project);
    }

    public void deleteById(Integer id) {
        projectMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            projectMapper.deleteById(id);
        }
    }

    public Project selectById(Integer id) {
        return projectMapper.selectById(id);
    }

    public List<Project> selectAll(Project project) {
        return projectMapper.selectAll(project);
    }

    public PageInfo<Project> selectPage(Project project, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Project> list = projectMapper.selectAll(project);
        return PageInfo.of(list);
    }

}
