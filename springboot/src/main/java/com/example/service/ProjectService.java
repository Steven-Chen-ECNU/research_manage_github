package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Project;
import com.example.exception.CustomException;
import com.example.mapper.ProjectMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 科研项目业务层方法
 */
@Service
public class ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    public void add(Project project) throws ParseException {
        project.setCode(DateUtil.parse(DateUtil.now()).toString("yyyyMMddHHmmss"));
        Account currentUser = TokenUtils.getCurrentUser();
        project.setTeacherId(currentUser.getId());
        project.setStatus("待审核");
        // 校验起讫时间
        long start = new SimpleDateFormat("yyyy-MM-dd").parse(project.getStart()).getTime();
        long end = new SimpleDateFormat("yyyy-MM-dd").parse(project.getEnd()).getTime();
        if(start >= end){
            throw new CustomException("-1","开始日期必须小于结束日期");
        }
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
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.TEACHER.name().equals(currentUser.getRole())){
            project.setTeacherId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Project> list = projectMapper.selectAll(project);
        return PageInfo.of(list);
    }

    public void check(Project project) {
        project.setTime(DateUtil.now());
        projectMapper.updateById(project);
    }

    public Integer countActiveTeacher() {
        return projectMapper.countActiveTeacher();
    }

}
