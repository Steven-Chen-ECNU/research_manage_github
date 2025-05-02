package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Activity;
import com.example.exception.CustomException;
import com.example.mapper.ActivityMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 业务层方法
 */
@Service
public class ActivityService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    @Resource
    private ActivityMapper activityMapper;

    public void add(Activity activity) {
        activityMapper.insert(activity);
    }

    public void updateById(Activity activity) {
        activityMapper.updateById(activity);
    }

    public void deleteById(Integer id) {
        activityMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            activityMapper.deleteById(id);
        }
    }

    public Activity selectById(Integer id) {
        return activityMapper.selectById(id);
    }

    public List<Activity> selectAll(Activity activity) {
        return activityMapper.selectAll(activity);
    }

    public PageInfo<Activity> selectPage(Activity activity, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectAll(activity);
        // 对学术活动的状态进行初始化
        for (Activity dbActivity : list) {
            try {
                long now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(DateUtil.now()).getTime();
                long start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dbActivity.getStart() + " 00:00:00").getTime();
                long end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dbActivity.getEnd() + " 23:59:59").getTime();
                if (now < start) {
                    dbActivity.setStatus("未开始");
                } else if (now > end) {
                    dbActivity.setStatus("已结束");
                } else {
                    dbActivity.setStatus("进行中");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("-1", "时间转换失败");
            }
        }
        return PageInfo.of(list);
    }

    /**
     * 获取各区科研活力数据
     * @return 各区活动数量统计
     */
    public List<Map<String, Object>> getResearchVitality() {
        try {
            logger.info("开始获取科研活力数据");
            List<Map<String, Object>> result = activityMapper.getResearchVitality();
            if (result == null) {
                logger.warn("查询结果为空");
                return new ArrayList<>();
            }
            logger.info("获取到 {} 条数据", result.size());
            return result;
        } catch (Exception e) {
            logger.error("获取科研活力数据失败", e);
            throw new CustomException("-1", "获取科研活力数据失败：" + e.getMessage());
        }
    }
}
