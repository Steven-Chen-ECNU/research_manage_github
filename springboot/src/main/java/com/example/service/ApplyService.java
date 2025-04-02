package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Apply;
import com.example.exception.CustomException;
import com.example.mapper.ApplyMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class ApplyService {

    @Resource
    private ApplyMapper applyMapper;

    public void add(Apply apply) {
        Account currentUser = TokenUtils.getCurrentUser();
        // 查询一下该教师有没有申请通过该学术活动
        List<Apply> list = applyMapper.selectByTeacherIdAndActivityId(currentUser.getId(), apply.getActivityId());
        if (CollectionUtil.isNotEmpty(list)) {
            throw new CustomException("-1", "您已申请过该学术活动，请勿重复提交");
        }
        apply.setTime(DateUtil.now());
        apply.setTeacherId(currentUser.getId());
        apply.setStatus("待审核");
        applyMapper.insert(apply);
    }

    public void updateById(Apply apply) {
        applyMapper.updateById(apply);
    }

    public void deleteById(Integer id) {
        applyMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            applyMapper.deleteById(id);
        }
    }

    public Apply selectById(Integer id) {
        return applyMapper.selectById(id);
    }

    public List<Apply> selectAll(Apply apply) {
        return applyMapper.selectAll(apply);
    }

    public PageInfo<Apply> selectPage(Apply apply, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.TEACHER.name().equals(currentUser.getRole())) {
            apply.setTeacherId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Apply> list = applyMapper.selectAll(apply);
        return PageInfo.of(list);
    }

    public void check(Apply apply) {
        apply.setCheckTime(DateUtil.now());
        applyMapper.updateById(apply);
    }
}
