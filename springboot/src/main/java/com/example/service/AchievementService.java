package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Achievement;
import com.example.exception.CustomException;
import com.example.mapper.AchievementMapper;
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
public class AchievementService {

    @Resource
    private AchievementMapper achievementMapper;

    public void add(Achievement achievement) throws ParseException {
        Account currentUser = TokenUtils.getCurrentUser();
        achievement.setTeacherId(currentUser.getId());
        achievement.setStatus("待审核");
        achievementMapper.insert(achievement);
    }

    public void updateById(Achievement achievement) {
        achievementMapper.updateById(achievement);
    }

    public void deleteById(Integer id) {
        achievementMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            achievementMapper.deleteById(id);
        }
    }

    public Achievement selectById(Integer id) {
        return achievementMapper.selectById(id);
    }

    public List<Achievement> selectAll(Achievement achievement) {
        return achievementMapper.selectAll(achievement);
    }

    public PageInfo<Achievement> selectPage(Achievement achievement, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.TEACHER.name().equals(currentUser.getRole())){
            achievement.setTeacherId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Achievement> list = achievementMapper.selectAll(achievement);
        return PageInfo.of(list);
    }

    public void check(Achievement achievement) {
        achievement.setTime(DateUtil.now());
        achievementMapper.updateById(achievement);
    }

}
